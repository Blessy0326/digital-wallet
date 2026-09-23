package com.blessy.digital_wallet.transaction;

import com.blessy.digital_wallet.account.Account;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionType type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balanceAfter;

    @Column(length = 255)
    private String description;

    @Column(length = 40)
    private String counterpartyAccountNumber;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected Transaction() {
        // required by JPA
    }

    public Transaction(Account account, TransactionType type, BigDecimal amount,
                       BigDecimal balanceAfter, String description,
                       String counterpartyAccountNumber) {
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.counterpartyAccountNumber = counterpartyAccountNumber;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public Account getAccount() { return account; }
    public TransactionType getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public String getDescription() { return description; }
    public String getCounterpartyAccountNumber() { return counterpartyAccountNumber; }
    public Instant getCreatedAt() { return createdAt; }
}