package ru.kirillova.bankservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.kirillova.bankservice.model.BankAccount;
import ru.kirillova.bankservice.repository.BankAccountRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BankAccountService {
    private final static Double MINUTE_INTEREST_RATE_PROC = 5.0;
    private final static Double MAX_INTEREST_RATE_PROC = 207.0;

    private final BankAccountRepository bankAccountRepository;
    private final InterestAccrualService interestAccrualService;

    @Scheduled(cron = "0 * * * * *")
    private void accrueInterest() {
        List<BankAccount> accounts = bankAccountRepository.findAll();
        Double minuteInterestRate = MINUTE_INTEREST_RATE_PROC / 100;
        Double maxInterestRate = MAX_INTEREST_RATE_PROC / 100;
        log.info("Accrued interest for {} accounts", accounts.size());
        for (BankAccount account : accounts) {
            interestAccrualService.accrueInterestOneAccount(account.getId(), minuteInterestRate, maxInterestRate);
        }
    }
}
