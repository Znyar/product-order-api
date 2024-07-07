package com.znyar.ecommerce.kafka;

import com.znyar.ecommerce.customer.CustomerResponse;
import com.znyar.ecommerce.order.PaymentMethod;
import com.znyar.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(

        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products

) {
}
