package ru.kirillova.bankservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.kirillova.bankservice.error.DataConflictException;
import ru.kirillova.bankservice.model.BankAccount;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface BankAccountRepository extends BaseRepository<BankAccount> {
    Optional<BankAccount> findByUserId(Integer userId);

    @Query("SELECT b FROM BankAccount b WHERE b.id = :bankAccountId and b.user.id = :userId")
    Optional<BankAccount> get(int userId, int bankAccountId);

    @Query("SELECT b FROM BankAccount b WHERE b.user.id= :userId")
    List<BankAccount> getAllByUser(@Param("userId") int userId);

    default BankAccount getBelonged(int userId, int bankAccountId) {
        return get(userId, bankAccountId).orElseThrow(
                () -> new DataConflictException("BankAccount id=" + bankAccountId + " does not exist or doesn't belong to User id=" + userId));
    }
}