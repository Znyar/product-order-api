package com.znyar.ecommerce.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductPurchaseRequest(

        @NotNull(message = "Product id mandatory")
        Integer productId,
        @Positive(message = "Quantity must be positive")
        double quantity

) {
}
