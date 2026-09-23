package com.blessy.digital_wallet.transaction;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TransferRequest(

        @NotNull(message = "Target account number is required")
        @NotBlank(message = "Target account number is required")
        String targetAccountNumber,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
        @Digits(integer = 17, fraction = 2, message = "Amount must have at most 2 decimal places")
        BigDecimal amount,

        @Size(max = 255, message = "Description must be at most 255 characters")
        String description
) {
}