package com.muravev.samokatimmonolit.service.impl;

import com.muravev.samokatimmonolit.entity.ChequeEntity;
import com.muravev.samokatimmonolit.entity.OrganizationTariffEntity;
import com.muravev.samokatimmonolit.entity.RentEntity;
import com.muravev.samokatimmonolit.model.OrganizationTariffType;
import com.muravev.samokatimmonolit.repo.RentRepo;
import com.muravev.samokatimmonolit.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Period;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Override
    @Transactional
    public void pay(RentEntity rent) {
        OrganizationTariffEntity tariff = rent.getTariff();
        if (tariff.getType() == OrganizationTariffType.MINUTE_BY_MINUTE) {
            ZonedDateTime startTime = rent.getStartTime();
            ZonedDateTime endTime = rent.getEndTime();
            Duration between = Duration.between(startTime, endTime);
            long minutes = between.toMinutes();
            BigDecimal price = tariff.getPrice();
            BigDecimal result = price.multiply(BigDecimal.valueOf(minutes));
            ChequeEntity cheque = new ChequeEntity()
                    .setPrice(result);
            rent.setCheque(cheque);
        }
        if (tariff.getType() == OrganizationTariffType.LONG_TERM) {
            BigDecimal price = tariff.getPrice();
            Duration between = Duration.between(rent.getStartTime(), rent.getEndTime());
            long days = between.toDays();
            BigDecimal result = price.multiply(days == 0 ? BigDecimal.ONE : BigDecimal.valueOf(days));
            ChequeEntity cheque = new ChequeEntity()
                    .setPrice(result);
            rent.setCheque(cheque);
        }
    }
}
