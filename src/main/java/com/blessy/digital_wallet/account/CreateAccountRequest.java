package com.blessy.digital_wallet.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateAccountRequest(

        @NotNull(message = "Owner id is required")
        Long ownerId,

        @NotNull(message = "Currency is required")
        @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter code such as EUR")
        String currency
) {
}