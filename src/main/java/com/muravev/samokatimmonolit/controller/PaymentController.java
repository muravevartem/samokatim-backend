package com.muravev.samokatimmonolit.controller;

import com.muravev.samokatimmonolit.integration.yookassa.model.response.Payment;
import com.muravev.samokatimmonolit.integration.yookassa.service.YooKassaPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final YooKassaPaymentService paymentService;

    @PostMapping(params = "rent")
    public Payment pay(@RequestParam long rentId) {
        return paymentService.createPayment(rentId, BigDecimal.valueOf(123), "Тестовая оплата");
    }

    @GetMapping("/{id}")
    public Payment get(@PathVariable String id) {
        return paymentService.getPaymentById(id);
    }
}
