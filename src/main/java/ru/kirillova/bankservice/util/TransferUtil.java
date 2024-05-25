package ru.kirillova.bankservice.util;

import lombok.experimental.UtilityClass;
import ru.kirillova.bankservice.model.Transfer;
import ru.kirillova.bankservice.to.TransferTo;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class TransferUtil {

    public static TransferTo createTo(Transfer transfer) {
        return new TransferTo(transfer.getId(),
                transfer.getSender().getId(),
                transfer.getReceiver().getId(),
                transfer.getAmount(),
                transfer.getTimestamp(),
                transfer.getStatus());
    }

    public static List<TransferTo> getTos(Collection<Transfer> transfers) {
        return transfers.stream().map(TransferUtil::createTo).toList();
    }
}