package com.blessy.digital_wallet.transaction;

import com.blessy.digital_wallet.account.Account;
import com.blessy.digital_wallet.account.AccountRepository;
import com.blessy.digital_wallet.common.exception.InvalidOperationException;
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

    @Transactional
    public TransactionResponse transfer(Long sourceAccountId, TransferRequest request) {
        Account source = findAccount(sourceAccountId);

        Account target = accountRepository.findByAccountNumber(request.targetAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Target account not found: " + request.targetAccountNumber()));

        if (source.getId().equals(target.getId())) {
            throw new InvalidOperationException("Cannot transfer to the same account");
        }

        if (!source.getCurrency().equals(target.getCurrency())) {
            throw new InvalidOperationException(
                    "Currency mismatch: source is " + source.getCurrency()
                            + ", target is " + target.getCurrency());
        }

        source.debit(request.amount());
        target.credit(request.amount());

        Transaction outgoing = new Transaction(
                source,
                TransactionType.TRANSFER_OUT,
                request.amount(),
                source.getBalance(),
                request.description(),
                target.getAccountNumber()
        );

        Transaction incoming = new Transaction(
                target,
                TransactionType.TRANSFER_IN,
                request.amount(),
                target.getBalance(),
                request.description(),
                source.getAccountNumber()
        );

        transactionRepository.save(incoming);
        return TransactionResponse.from(transactionRepository.save(outgoing));
    }

    private Account findAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));
    }
}