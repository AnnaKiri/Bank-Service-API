package ru.kirillova.bankservice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kirillova.bankservice.model.BankAccount;
import ru.kirillova.bankservice.model.Transfer;
import ru.kirillova.bankservice.repository.BankAccountRepository;
import ru.kirillova.bankservice.repository.TransferRepository;
import ru.kirillova.bankservice.repository.UserRepository;

@Service
@AllArgsConstructor
public class TransferService {

    private final TransferRepository transferRepository;
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final TransferSaveService transferSaveService;
    private final LockRegistry lockRegistry;

    @Transactional
    public Transfer makeTransfer(Integer senderUserId, Integer receiverUserId, Double amount) {
        Transfer transfer = new Transfer(userRepository.getReferenceById(senderUserId),
                userRepository.getReferenceById(receiverUserId),
                amount);

        if (senderUserId.equals(receiverUserId)) {
            transfer.setStatus("FAIL");
            transferSaveService.saveTransferWithNewTransaction(transfer);
            throw new IllegalArgumentException("You can't transfer to yourself");
        }

        BankAccount senderBankAccount = bankAccountRepository.get(senderUserId);
        BankAccount receiverBankAccount = bankAccountRepository.get(receiverUserId);

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
                    transfer.setStatus("FAIL");
                    transferSaveService.saveTransferWithNewTransaction(transfer);
                    throw new IllegalArgumentException("Insufficient balance");
                }

                senderBankAccount.setBalance(senderBankAccount.getBalance() - amount);
                receiverBankAccount.setBalance(receiverBankAccount.getBalance() + amount);

                bankAccountRepository.save(receiverBankAccount);
                bankAccountRepository.save(senderBankAccount);

                transfer.setStatus("SUCCESS");
                return transferRepository.save(transfer);
            }
        }
    }
}
