package com.blessy.digital_wallet.transaction;

import com.blessy.digital_wallet.account.Account;
import com.blessy.digital_wallet.account.AccountRepository;
import com.blessy.digital_wallet.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository,
                              TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public TransactionResponse deposit(Long accountId, AmountRequest request) {
        Account account = findAccount(accountId);

        account.credit(request.amount());

        Transaction transaction = new Transaction(
                account,
                TransactionType.DEPOSIT,
                request.amount(),
                account.getBalance(),
                request.description(),
                null
        );
        return TransactionResponse.from(transactionRepository.save(transaction));
    }

    @Transactional
    public TransactionResponse withdraw(Long accountId, AmountRequest request) {
        Account account = findAccount(accountId);

        account.debit(request.amount());

        Transaction transaction = new Transaction(
                account,
                TransactionType.WITHDRAWAL,
                request.amount(),
                account.getBalance(),
                request.description(),
                null
        );
        return TransactionResponse.from(transactionRepository.save(transaction));
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponse> getTransactions(Long accountId, Pageable pageable) {
        if (!accountRepository.existsById(accountId)) {
            throw new ResourceNotFoundException("Account not found: " + accountId);
        }
        return transactionRepository
                .findByAccountIdOrderByCreatedAtDesc(accountId, pageable)
                .map(TransactionResponse::from);
    }

    private Account findAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));
    }
}