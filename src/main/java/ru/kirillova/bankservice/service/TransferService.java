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

    private TransferRepository transferRepository;
    private UserRepository userRepository;
    private BankAccountRepository bankAccountRepository;

    @Transactional
    public synchronized Transfer makeTransfer(Integer senderId, Integer receiverId, Double amount) {
        Transfer transfer = new Transfer(userRepository.getReferenceById(senderId), userRepository.getReferenceById(receiverId), amount);

        BankAccount senderBankAccount = bankAccountRepository.get(senderId);
        BankAccount receiverBankAccount = bankAccountRepository.get(receiverId);
        if (senderBankAccount.getBalance() < amount) {
            transfer.setStatus("FAIL");
            transferRepository.save(transfer);
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
