package com.blessy.digital_wallet.account;

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

    public Long getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public User getOwner() { return owner; }
    public BigDecimal getBalance() { return balance; }
    public String getCurrency() { return currency; }
    public Instant getCreatedAt() { return createdAt; }
}