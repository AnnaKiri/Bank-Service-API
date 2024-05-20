package ru.kirillova.bankservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.kirillova.bankservice.error.DataConflictException;
import ru.kirillova.bankservice.model.Transfer;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface TransferRepository extends JpaRepository<Transfer, Integer> {
    @Query("SELECT t FROM Transfer t WHERE t.id = :transferId and t.sender.id = :userId")
    Optional<Transfer> get(int userId, int transferId);

    @Query("SELECT t FROM Transfer t WHERE t.sender.id= :userId")
    List<Transfer> getAllByUser(int userId);

    default Transfer getBelonged(int userId, int transferId) {
        return get(userId, transferId).orElseThrow(
                () -> new DataConflictException("Transfer id=" + transferId + " does not exist or doesn't belong to User id=" + userId));
    }
}