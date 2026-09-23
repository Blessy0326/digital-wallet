package com.blessy.digital_wallet.account;

import com.blessy.digital_wallet.common.exception.InsufficientFundsException;
import com.blessy.digital_wallet.user.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, unique = true, updatable = false)
    private String accountNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected Account() {
        // required by JPA
    }

    public Account(String accountNumber, User owner, String currency) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.currency = currency;
        this.balance = BigDecimal.ZERO;
        this.createdAt = Instant.now();
    }

    public void credit(BigDecimal amount) {
        validateAmount(amount);
        this.balance = this.balance.add(amount);
    }

    public void debit(BigDecimal amount) {
        validateAmount(amount);
        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException(
                    "Insufficient funds in account " + accountNumber
                            + ": balance " + balance + ", requested " + amount);
        }
        this.balance = this.balance.subtract(amount);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

    public Long getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public User getOwner() { return owner; }
    public BigDecimal getBalance() { return balance; }
    public String getCurrency() { return currency; }
    public Instant getCreatedAt() { return createdAt; }
    public Long getVersion() { return version; }
}