package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.*;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.integration.yookassa.model.PaymentCurrency;
import com.muravev.samokatimmonolit.integration.yookassa.model.PaymentItem;
import com.muravev.samokatimmonolit.integration.yookassa.model.response.Payment;
import com.muravev.samokatimmonolit.integration.yookassa.model.response.PaymentAmount;
import com.muravev.samokatimmonolit.integration.yookassa.service.YooKassaPaymentService;
import com.muravev.samokatimmonolit.model.DespositStatus;
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
    private static final String RETURN_URL = "http://localhost:3000";
    private static final double SECONDS_PER_DAY = 86400;


    private final PaymentRepository paymentRepository;
    private final DepositRepo depositRepo;
    private final YooKassaPaymentService providerPayment;
    private final InventorySaver inventorySaver;
    private final InventoryReader inventoryReader;


    @Override
    @Transactional
    public PaymentOptionsOut pay(RentEntity rent) {
        assert rent.getCheque() == null;
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
                List.of(
                        PaymentItem.builder()
                                .amount(new PaymentAmount(resultPrice, PaymentCurrency.RUB))
                                .description("Аренда инвентаря (%s)м".formatted(minutes))
                                .quantity("1")
                                .vatCode(1)
                                .build(),
                        PaymentItem.builder()
                                .amount(new PaymentAmount(initialPrice, PaymentCurrency.RUB))
                                .description("Начало аренды")
                                .quantity("1")
                                .vatCode(1)
                                .build()
                ),
                RETURN_URL,
                rent.getClient()
        );
        savedPayment.setStatus(PaymentStatus.PENDING)
                .setBankId(paymentResponse.id());
        return new PaymentOptionsOut(rent.getId(), paymentResponse.confirmation().url());
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
                List.of(
                        PaymentItem.builder()
                                .amount(new PaymentAmount(resultPrice, PaymentCurrency.RUB))
                                .description("Аренда инвентаря (%s)д".formatted(days))
                                .quantity("1")
                                .vatCode(1)
                                .build()
                ),
                RETURN_URL,
                rent.getClient()
        );
        savedPayment.setStatus(PaymentStatus.PENDING)
                .setBankId(paymentResponse.id());
        return new PaymentOptionsOut(rent.getId(), paymentResponse.confirmation().url());
    }


    @Override
    @Transactional
    public PaymentOptionsOut deposit(RentEntity rent) {
        assert rent.getDeposit() == null;

        OrganizationTariffEntity tariff = rent.getTariff();
        BigDecimal depositPrice = tariff.getDeposit() == null
                ? BigDecimal.ONE
                :  tariff.getDeposit();

        DepositEntity deposit = new DepositEntity();
        deposit.setRent(rent)
                .setStatus(DespositStatus.CREATING)
                .setPrice(depositPrice);
        DepositEntity savedDeposit = depositRepo.save(deposit);
        String description = "Залог аренды #" + rent.getId();
        try {
            Payment payment = providerPayment.hold(
                    rent.getId().toString(),
                    depositPrice,
                    description,
                    List.of(
                            PaymentItem.builder()
                                    .amount(new PaymentAmount(depositPrice, PaymentCurrency.RUB))
                                    .description(description)
                                    .vatCode(1)
                                    .quantity("1")
                                    .build()
                    ),
                    RETURN_URL,
                    rent.getClient()
            );
            savedDeposit.setStatus(DespositStatus.PENDING)
                    .setBankId(payment.id());
            return new PaymentOptionsOut(
                    rent.getId(),
                    payment.confirmation().url()
            );
        } catch (Exception e) {
            throw new ApiException(StatusCode.DEPOSIT_CREATING_ERROR);
        }
    }

    @Scheduled(fixedDelay = 2000L)
    @Transactional
    public void handleDeposits() {
        List<DepositEntity> pendingDeposits = depositRepo.findAllByStatus(DespositStatus.PENDING);
        pendingDeposits.forEach(this::handleDeposit);
    }

    private void handleDeposit(DepositEntity deposit) {
        Payment payment = providerPayment.getPaymentById(deposit.getBankId());
        final RentEntity rent = deposit.getRent();
        switch (payment.status()) {
            case CANCELED -> {
                rent.setStatus(RentStatus.CANCELED);
                deposit.setStatus(DespositStatus.CANCELED);
                inventorySaver.changeStatus(rent.getInventory(), InventoryStatus.PENDING);
            }
            case SUCCEEDED -> {
                rent.setStatus(RentStatus.ACTIVE);
                rent.setStartTime(ZonedDateTime.now());
                deposit.setStatus(DespositStatus.HOLD);
            }
            case WAITING_FOR_CAPTURE -> {
                providerPayment.debit(deposit.getBankId());
            }
            case PENDING -> {}
        }
    }

    @Scheduled(fixedDelay = 2000L)
    @Transactional
    public void handlePayments() {
        List<PaymentEntity> pendingPayments = paymentRepository.findAllByStatus(PaymentStatus.PENDING);
        pendingPayments.forEach(this::handlePayment);
    }

    private void handlePayment(PaymentEntity payment) {
        Payment existedPayment = providerPayment.getPaymentById(payment.getBankId());
        final RentEntity rent = payment.getRent();
        switch (existedPayment.status()) {
            case CANCELED -> {
                rent.setStatus(RentStatus.CANCELED);
                payment.setStatus(PaymentStatus.CANCELED);
                inventorySaver.changeStatus(rent.getInventory(), InventoryStatus.PENDING);
            }
            case SUCCEEDED -> {
                rent.setStatus(RentStatus.COMPLETED);
                rent.setStartTime(ZonedDateTime.now());
                payment.setStatus(PaymentStatus.COMPLETED);
            }
            case WAITING_FOR_CAPTURE -> {
                providerPayment.debit(payment.getBankId());
                payment.setStatus(PaymentStatus.CONFIRMED);
            }
            case PENDING -> {}
        }
    }
}
