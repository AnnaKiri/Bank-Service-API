package ru.kirillova.bankservice.repository;

import org.springframework.transaction.annotation.Transactional;
import ru.kirillova.bankservice.model.BankAccount;

import java.util.Optional;

@Transactional(readOnly = true)
public interface BankAccountRepository extends BaseRepository<BankAccount> {
    Optional<BankAccount> findByUserId(Integer userId);

}