package ru.kirillova.bankservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kirillova.bankservice.model.BankAccount;
import ru.kirillova.bankservice.repository.BankAccountRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class InterestAccrualService {

    private final BankAccountRepository bankAccountRepository;
    private final LockRegistry lockRegistry;

    @Transactional
    protected void accrueInterestOneAccount(Integer id, Double minuteInterestRate, Double maxInterestRate) {
        log.info("Starting interest accrual for account id={}", id);
        Object lock = lockRegistry.getLock(id);
        synchronized (lock) {
            Optional<BankAccount> bankAccountOpt = bankAccountRepository.findById(id);
            if (bankAccountOpt.isEmpty()) {
                log.warn("Account with id={} not found", id);
                return;
            }

            BankAccount account = bankAccountOpt.get();
            Double currentBalance = account.getBalance();
            Double balanceWithInterest = currentBalance + currentBalance * minuteInterestRate;
            Double diffWithInitialBalance = balanceWithInterest - account.getInitialBalance();
            Double maxIncreasingByInterest = account.getInitialBalance() * maxInterestRate;
            Double increasingAllowed = Math.max(maxIncreasingByInterest - diffWithInitialBalance, 0.0);
            Double newBalance = Math.min(balanceWithInterest, currentBalance + increasingAllowed);

            log.info("Account id={}, current balance={}, balance with interest={}, allowed increase={}, new balance={}",
                    id, currentBalance, balanceWithInterest, increasingAllowed, newBalance);

            account.setBalance(newBalance);
            bankAccountRepository.save(account);

            log.info("Interest successfully accrued for account id={}, new balance={}", id, newBalance);
        }
    }
}
