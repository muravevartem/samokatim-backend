package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.*;
import com.muravev.samokatimmonolit.entity.user.ClientEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.integration.yookassa.model.response.Payment;
import com.muravev.samokatimmonolit.integration.yookassa.model.response.Refund;
import com.muravev.samokatimmonolit.integration.yookassa.service.YooKassaPaymentService;
import com.muravev.samokatimmonolit.model.DepositStatus;
import com.muravev.samokatimmonolit.model.InventoryStatus;
import com.muravev.samokatimmonolit.model.PaymentStatus;
import com.muravev.samokatimmonolit.model.RentStatus;
import com.muravev.samokatimmonolit.model.out.PaymentOptionsOut;
import com.muravev.samokatimmonolit.repo.DepositRepo;
import com.muravev.samokatimmonolit.repo.PaymentRepository;
import com.muravev.samokatimmonolit.service.InventoryReader;
import com.muravev.samokatimmonolit.service.InventorySaver;
import com.muravev.samokatimmonolit.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private static final String RETURN_URL = "https://1304294-cu57808.tw1.ru/home";
    private static final double SECONDS_PER_DAY = 86400;


    private final PaymentRepository paymentRepository;
    private final DepositRepo depositRepo;
    private final YooKassaPaymentService providerPayment;
    private final InventorySaver inventorySaver;
    private final InventoryReader inventoryReader;


    @Override
    @Transactional
    public PaymentOptionsOut pay(RentEntity rent) {
        PaymentEntity cheque = rent.getCheque();
        if (cheque != null) {
            return new PaymentOptionsOut(rent.getId(), cheque.getUrl());
        }
        OrganizationTariffEntity tariff = rent.getTariff();
        PaymentOptionsOut paymentOptionsOut = switch (tariff.getType()) {
            case MINUTE_BY_MINUTE -> payByMinuteByMinuteTariff(rent);
            case LONG_TERM -> payByLongTermTariff(rent);
            default -> throw new ApiException(StatusCode.TARIFF_NOT_FOUND);
        };
        return paymentOptionsOut;
    }

    private PaymentOptionsOut payByMinuteByMinuteTariff(RentEntity rent) {
        OrganizationTariffEntity tariff = rent.getTariff();
        BigDecimal initialPrice = tariff.getInitialPrice() == null
                ? BigDecimal.ZERO
                : tariff.getInitialPrice();
        BigDecimal price = tariff.getPrice();
        ZonedDateTime startTime = rent.getStartTime();
        ZonedDateTime endTime = rent.getEndTime();
        long minutes = Duration.between(startTime, endTime).toMinutes();

        BigDecimal resultPrice = price.multiply(BigDecimal.valueOf(minutes));
        BigDecimal result = resultPrice.add(initialPrice);

        PaymentEntity payment = new PaymentEntity()
                .setRent(rent)
                .setStatus(PaymentStatus.CREATING)
                .setPrice(result);

        PaymentEntity savedPayment = paymentRepository.save(payment);
        Payment paymentResponse = providerPayment.hold(
                rent.getId().toString(),
                result,
                "Аренда инвентаря #" + rent.getId(),
                RETURN_URL,
                rent.getClient()
        );
        savedPayment.setStatus(PaymentStatus.PENDING)
                .setBankId(paymentResponse.id())
                .setUrl(paymentResponse.confirmation().url());
        return new PaymentOptionsOut(rent.getId(), savedPayment.getUrl());
    }

    private PaymentOptionsOut payByLongTermTariff(RentEntity rent) {
        OrganizationTariffEntity tariff = rent.getTariff();
        BigDecimal price = tariff.getPrice();
        ZonedDateTime startTime = rent.getStartTime();
        ZonedDateTime endTime = rent.getEndTime();
        long seconds = Duration.between(startTime, endTime).toSeconds();
        int days = (int) Math.ceil(seconds / SECONDS_PER_DAY);


        BigDecimal resultPrice = price.multiply(BigDecimal.valueOf(days));

        PaymentEntity payment = new PaymentEntity()
                .setRent(rent)
                .setStatus(PaymentStatus.CREATING)
                .setPrice(resultPrice);

        PaymentEntity savedPayment = paymentRepository.save(payment);
        Payment paymentResponse = providerPayment.hold(
                rent.getId().toString(),
                resultPrice,
                "Аренда инвентаря #" + rent.getId(),
                RETURN_URL,
                rent.getClient()
        );
        savedPayment.setStatus(PaymentStatus.PENDING)
                .setBankId(paymentResponse.id())
                .setUrl(paymentResponse.confirmation().url());
        return new PaymentOptionsOut(rent.getId(), savedPayment.getUrl());
    }


    @Override
    @Transactional
    public PaymentOptionsOut deposit(RentEntity rent) {
        assert rent.getDeposit() == null;

        OrganizationTariffEntity tariff = rent.getTariff();
        BigDecimal depositPrice = tariff.getDeposit() == null
                ? BigDecimal.ONE
                : tariff.getDeposit();

        DepositEntity deposit = new DepositEntity();
        deposit.setRent(rent)
                .setStatus(DepositStatus.CREATING)
                .setPrice(depositPrice);
        DepositEntity savedDeposit = depositRepo.save(deposit);
        String description = "Залог аренды #" + rent.getId();
        Payment payment = providerPayment.hold(
                rent.getId().toString(),
                depositPrice,
                description,
                RETURN_URL,
                rent.getClient()
        );
        savedDeposit.setStatus(DepositStatus.PENDING)
                .setBankId(payment.id())
                .setUrl(payment.confirmation().url());
        return new PaymentOptionsOut(
                rent.getId(),
                savedDeposit.getUrl()
        );
    }

    @Scheduled(fixedDelay = 5000L)
    @Transactional
    public void handleDeposits() {
        List<DepositEntity> pendingDeposits = depositRepo.findAllByStatus(DepositStatus.PENDING);
        pendingDeposits.forEach(this::handleDeposit);
    }

    @Scheduled(fixedDelay = 5000L)
    @Transactional
    public void handlePayments() {
        List<PaymentEntity> pendingPayments = paymentRepository.findAllByStatusIsIn(List.of(PaymentStatus.PENDING, PaymentStatus.CONFIRMED));
        pendingPayments.forEach(this::handlePayment);
    }

    @Scheduled(fixedDelay = 5000L)
    @Transactional
    public void handleRefundDeposits() {
        List<DepositEntity> pendingDeposits = depositRepo.findAllByStatus(DepositStatus.REFUNDING);
        pendingDeposits.forEach(this::handleRefundDeposit);
    }


    private void handleDeposit(DepositEntity deposit) {
        Payment payment = providerPayment.getPaymentById(deposit.getBankId());
        final RentEntity rent = deposit.getRent();
        switch (payment.status()) {
            case CANCELED -> {
                rent.setStatus(RentStatus.CANCELED);
                deposit.setStatus(DepositStatus.CANCELED);
                inventorySaver.changeStatus(rent.getInventory(), InventoryStatus.PENDING);
            }
            case SUCCEEDED -> {
                rent.setStatus(RentStatus.ACTIVE);
                rent.setStartTime(ZonedDateTime.now());
                deposit.setStatus(DepositStatus.HOLD);
            }
            case WAITING_FOR_CAPTURE -> {
                if (rent.getStatus() == RentStatus.STARTING) {
                    providerPayment.debit(deposit.getBankId());
                }
            }
            case PENDING -> {
            }
        }
    }

    private void handleRefundDeposit(DepositEntity deposit) {
        Refund refund = providerPayment.getRefundById(deposit.getRefundBankId());
        switch (refund.status()) {
            case CANCELED -> {
                deposit.setStatus(DepositStatus.HOLD);
            }
            case SUCCEEDED -> {
                deposit.setStatus(DepositStatus.REFUNDED);
                RentEntity rent = deposit.getRent();
                rent.setStatus(RentStatus.COMPLETED);
            }
            case PENDING -> {
            }
        }
    }


    private void handlePayment(PaymentEntity payment) {
        Payment existedPayment = providerPayment.getPaymentById(payment.getBankId());
        final RentEntity rent = payment.getRent();
        switch (existedPayment.status()) {
            case CANCELED -> {
                rent.setStatus(RentStatus.UNPAID);
                payment.setStatus(PaymentStatus.CANCELED);
            }
            case SUCCEEDED -> {
                payment.setStatus(PaymentStatus.COMPLETED);
                DepositEntity deposit = rent.getDeposit();
                if (deposit.getStatus() == DepositStatus.HOLD) {
                    String description = "Возврат залога аренды #%s".formatted(rent.getId());
                    Refund refund = providerPayment.refund(rent.getId().toString(),
                            deposit.getBankId(),
                            deposit.getPrice(),
                            description,
                            Hibernate.unproxy(rent.getClient(), ClientEntity.class)
                    );
                    deposit.setRefundBankId(refund.id())
                            .setStatus(DepositStatus.REFUNDING);
                }

            }
            case WAITING_FOR_CAPTURE -> {
                providerPayment.debit(payment.getBankId());
                payment.setStatus(PaymentStatus.CONFIRMED);
            }
            case PENDING -> {
            }
        }
    }
}
