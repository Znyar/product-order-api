package com.znyar.ecommerce.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
        String id,
        @NotNull(message = "Customer first name is required")
        String firstname,
        @NotNull(message = "Customer last name is required")
        String lastname,
        @NotNull(message = "Customer email is required")
        @Email(message = "Customer email is invalid")
        String email,
        Address address
) {
}
