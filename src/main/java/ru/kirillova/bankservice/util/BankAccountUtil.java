package ru.kirillova.bankservice.util;

import ru.kirillova.bankservice.model.BankAccount;
import ru.kirillova.bankservice.to.BankAccountTo;

import java.util.Collection;
import java.util.List;

public class BankAccountUtil {

    public static BankAccountTo createTo(BankAccount bankAccount) {
        return new BankAccountTo(bankAccount.getId(), bankAccount.getBalance());
    }

    public static List<BankAccountTo> getTos(Collection<BankAccount> bankAccounts) {
        return bankAccounts.stream().map(BankAccountUtil::createTo).toList();
    }
}