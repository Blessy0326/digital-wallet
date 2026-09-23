package com.blessy.digital_wallet.account;

import com.blessy.digital_wallet.common.exception.ResourceNotFoundException;
import com.blessy.digital_wallet.user.User;
import com.blessy.digital_wallet.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        User owner = userRepository.findById(request.ownerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.ownerId()));

        Account account = new Account(generateAccountNumber(), owner, request.currency());
        Account saved = accountRepository.save(account);
        return AccountResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + id));
        return AccountResponse.from(account);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAccountsByOwner(Long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            throw new ResourceNotFoundException("User not found: " + ownerId);
        }
        return accountRepository.findByOwnerId(ownerId)
                .stream()
                .map(AccountResponse::from)
                .toList();
    }

    private String generateAccountNumber() {
        return "ACC-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }
}