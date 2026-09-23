package com.blessy.digital_wallet.transaction;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponse(
        Long id,
        Long accountId,
        String accountNumber,
        TransactionType type,
        BigDecimal amount,
        BigDecimal balanceAfter,
        String description,
        String counterpartyAccountNumber,
        Instant createdAt
) {
    public static TransactionResponse from(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAccount().getId(),
                transaction.getAccount().getAccountNumber(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getBalanceAfter(),
                transaction.getDescription(),
                transaction.getCounterpartyAccountNumber(),
                transaction.getCreatedAt()
        );
    }
}