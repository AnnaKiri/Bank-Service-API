package ru.kirillova.bankservice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.kirillova.bankservice.model.Transfer;
import ru.kirillova.bankservice.repository.TransferRepository;

@Service
@AllArgsConstructor
public class TransferSaveService {

    private final TransferRepository transferRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveTransferWithNewTransaction(Transfer transfer) {
        transferRepository.save(transfer);
    }
}
