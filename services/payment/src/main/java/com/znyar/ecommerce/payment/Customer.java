package com.znyar.ecommerce.payment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record Customer(

        String id,
        @NotNull(message = "First name is required")
        String firstname,
        @NotNull(message = "Last name is required")
        String lastname,
        @Email(message = "The customer email is invalid")
        String email

) {
}
