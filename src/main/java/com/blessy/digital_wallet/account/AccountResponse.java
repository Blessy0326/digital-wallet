package com.blessy.digital_wallet.account;

import java.math.BigDecimal;
import java.time.Instant;

public record AccountResponse(
        Long id,
        String accountNumber,
        Long ownerId,
        String ownerName,
        BigDecimal balance,
        String currency,
        Instant createdAt
) {
    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getOwner().getId(),
                account.getOwner().getFullName(),
                account.getBalance(),
                account.getCurrency(),
                account.getCreatedAt()
        );
    }
}