package ru.kirillova.bankservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.kirillova.bankservice.model.Transfer;
import ru.kirillova.bankservice.repository.TransferRepository;

@Service
@AllArgsConstructor
@Slf4j
public class TransferSaveService {

    private final TransferRepository transferRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveTransferWithNewTransaction(Transfer transfer) {
        log.info("Saving transfer with new transaction: SenderID={}, ReceiverID={}, Amount={}, Status={}",
                transfer.getSender().getId(), transfer.getReceiver().getId(), transfer.getAmount(), transfer.getStatus());
        transferRepository.save(transfer);
        log.info("Transfer saved successfully with ID: {}", transfer.getId());
    }
}
