package com.muravev.samokatimmonolit.integration.yookassa.model.request;

import com.muravev.samokatimmonolit.integration.yookassa.model.PaymentItem;
import lombok.Builder;

import java.util.List;

@Builder
public record ReceiptRequest(
        ReceiptCustomerRequest customer,
        List<PaymentItem> items
) {
}
