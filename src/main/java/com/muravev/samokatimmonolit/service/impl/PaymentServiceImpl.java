package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.ChequeEntity;
import com.muravev.samokatimmonolit.entity.OrganizationTariffEntity;
import com.muravev.samokatimmonolit.entity.RentEntity;
import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import com.muravev.samokatimmonolit.integration.yookassa.model.response.Payment;
import com.muravev.samokatimmonolit.integration.yookassa.service.YooKassaPaymentService;
import com.muravev.samokatimmonolit.model.OrganizationTariffType;
import com.muravev.samokatimmonolit.model.out.PaymentOptionsOut;
import com.muravev.samokatimmonolit.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final YooKassaPaymentService providerPayment;

    @Override
    @Transactional
    public PaymentOptionsOut pay(RentEntity rent) {
        OrganizationTariffEntity tariff = rent.getTariff();
        if (tariff.getType() == OrganizationTariffType.MINUTE_BY_MINUTE) {
            ZonedDateTime startTime = rent.getStartTime();
            ZonedDateTime endTime = rent.getEndTime();
            Duration between = Duration.between(startTime, endTime);
            long minutes = between.toMinutes();
            BigDecimal price = tariff.getPrice();
            BigDecimal result = price.multiply(BigDecimal.valueOf(minutes));
            Payment payment = providerPayment.createPayment(rent.getId(), result, "Самокатим. Аренда #%s".formatted(rent.getId()));
            ChequeEntity cheque = new ChequeEntity()
                    .setPrice(result)
                    .setPaid(false)
                    .setBankChequeNumber(payment.id());
            rent.setCheque(cheque);
            return new PaymentOptionsOut(payment.confirmation().confirmationUrl());
        }
        if (tariff.getType() == OrganizationTariffType.LONG_TERM) {
            BigDecimal price = tariff.getPrice();
            Duration between = Duration.between(rent.getStartTime(), rent.getEndTime());
            long days = between.toDays();
            BigDecimal result = price.multiply(days == 0 ? BigDecimal.ONE : BigDecimal.valueOf(days));
            Payment payment = providerPayment.createPayment(rent.getId(), result, "Самокатим. Аренда #%s".formatted(rent.getId()));
            ChequeEntity cheque = new ChequeEntity()
                    .setPrice(result)
                    .setPaid(false)
                    .setBankChequeNumber(payment.id());
            rent.setCheque(cheque);
            return new PaymentOptionsOut(payment.confirmation().confirmationUrl());
        }
        throw new ApiException(StatusCode.BAD_REQUEST);
    }
}
