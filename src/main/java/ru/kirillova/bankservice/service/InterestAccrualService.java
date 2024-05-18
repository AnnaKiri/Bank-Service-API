package ru.kirillova.bankservice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kirillova.bankservice.model.BankAccount;
import ru.kirillova.bankservice.repository.BankAccountRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class InterestAccrualService {

    private final BankAccountRepository bankAccountRepository;
    private final LockRegistry lockRegistry;

    @Transactional
    protected void accrueInterestOneAccount(Integer id, Double minuteInterestRate, Double maxInterestRate) {
        Object lock = lockRegistry.getLock(id);
        synchronized (lock) {
            Optional<BankAccount> bankAccount = bankAccountRepository.findById(id);
            if (bankAccount.isEmpty()) {
                return;
            }

            BankAccount account = bankAccount.get();

            Double currentBalance = account.getBalance();
            Double balanceWithInterest = currentBalance + currentBalance * minuteInterestRate;
            Double diffWithInitialBalance = balanceWithInterest - account.getInitialBalance();
            Double maxIncreasingByInterest = account.getInitialBalance() * maxInterestRate;
            Double increasingAllowed = Math.max(maxIncreasingByInterest - diffWithInitialBalance, 0.0);
            Double newBalance = Math.min(balanceWithInterest, currentBalance + increasingAllowed);

            account.setBalance(newBalance);
            bankAccountRepository.save(account);
        }
    }
}
