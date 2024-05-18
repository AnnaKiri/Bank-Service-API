package ru.kirillova.bankservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kirillova.bankservice.model.BankAccount;
import ru.kirillova.bankservice.repository.BankAccountRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BankAccountService {
    private final static Double MINUTE_INTEREST_RATE_PROC = 5.0;

    private final BankAccountRepository bankAccountRepository;

    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void accrueInterest() {
        List<BankAccount> accounts = bankAccountRepository.findAll();
        Double minuteInterestRate = MINUTE_INTEREST_RATE_PROC / 100;
        for (BankAccount account : accounts) {
            Double currentBalance = account.getBalance();
            Double interest = currentBalance * minuteInterestRate;
            account.setBalance(currentBalance + interest);
        }
        bankAccountRepository.saveAll(accounts);
        log.info("Accrued interest for {} accounts", accounts.size());
    }
}
