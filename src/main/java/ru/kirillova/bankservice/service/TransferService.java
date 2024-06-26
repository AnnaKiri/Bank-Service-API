package ru.kirillova.bankservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kirillova.bankservice.model.BankAccount;
import ru.kirillova.bankservice.model.Transfer;
import ru.kirillova.bankservice.repository.BankAccountRepository;
import ru.kirillova.bankservice.repository.TransferRepository;
import ru.kirillova.bankservice.repository.UserRepository;

@Service
@AllArgsConstructor
@Slf4j
public class TransferService {
    public static final String TRANSFER_TO_YOURSELF_ERROR = "You can't transfer to yourself";
    public static final String NEGATIVE_AMOUNT_OF_TRANSFER_ERROR = "You can't transfer negative amount of money";
    public static final String INSUFFICIENT_BALANCE_ERROR = "Insufficient balance";

    private final TransferRepository transferRepository;
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final TransferSaveService transferSaveService;
    private final LockRegistry lockRegistry;

    @Transactional
    public Transfer makeTransfer(Integer senderUserId, Integer receiverUserId, Double amount) {
        log.info("Initiating transfer from user {} to user {} with amount {}", senderUserId, receiverUserId, amount);

        Transfer transfer = new Transfer(userRepository.getReferenceById(senderUserId),
                userRepository.getReferenceById(receiverUserId),
                amount);

        if (senderUserId.equals(receiverUserId)) {
            logBadCase(TRANSFER_TO_YOURSELF_ERROR, transfer);
        }

        if (amount < 0) {
            logBadCase(NEGATIVE_AMOUNT_OF_TRANSFER_ERROR, transfer);
        }

        BankAccount senderBankAccount = bankAccountRepository.getByUserId(senderUserId);
        BankAccount receiverBankAccount = bankAccountRepository.getByUserId(receiverUserId);

        Object lock1;
        Object lock2;

        if (senderBankAccount.getId() < receiverBankAccount.getId()) {
            lock1 = lockRegistry.getLock(senderBankAccount.getId());
            lock2 = lockRegistry.getLock(receiverBankAccount.getId());
        } else {
            lock1 = lockRegistry.getLock(receiverBankAccount.getId());
            lock2 = lockRegistry.getLock(senderBankAccount.getId());
        }

        synchronized (lock1) {
            synchronized (lock2) {
                if (senderBankAccount.getBalance() < amount) {
                    logBadCase(INSUFFICIENT_BALANCE_ERROR, transfer);
                }

                log.info("Processing transfer: deducting amount from sender and adding to receiver");
                senderBankAccount.setBalance(senderBankAccount.getBalance() - amount);
                receiverBankAccount.setBalance(receiverBankAccount.getBalance() + amount);

                bankAccountRepository.save(receiverBankAccount);
                bankAccountRepository.save(senderBankAccount);

                transfer.setStatus("SUCCESS");
                Transfer savedTransfer = transferRepository.save(transfer);
                log.info("Transfer successful: {}", savedTransfer);
                return savedTransfer;
            }
        }
    }

    private void logBadCase(String message, Transfer transfer) throws IllegalArgumentException {
        transfer.setStatus("FAIL");
        transferSaveService.saveTransferWithNewTransaction(transfer);
        log.error("Transfer failed: {}", message);
        throw new IllegalArgumentException(message);
    }
}
