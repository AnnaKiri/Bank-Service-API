package ru.kirillova.bankservice.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.kirillova.bankservice.model.Transfer;
import ru.kirillova.bankservice.repository.BankAccountRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.kirillova.bankservice.BankAccountTestData.BANK_ACCOUNT_1_AFTER_1_TRANSFER;
import static ru.kirillova.bankservice.BankAccountTestData.BANK_ACCOUNT_2_AFTER_1_TRANSFER;
import static ru.kirillova.bankservice.BankAccountTestData.BANK_ACCOUNT_MATCHER;
import static ru.kirillova.bankservice.TransferTestData.TRANSFER_MATCHER;
import static ru.kirillova.bankservice.TransferTestData.transfer1;
import static ru.kirillova.bankservice.UserTestData.USER1_ID;
import static ru.kirillova.bankservice.UserTestData.user1;
import static ru.kirillova.bankservice.UserTestData.user2;

@SpringBootTest
@Transactional
public class TransferServiceTest {

    @Autowired
    private TransferService service;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Test
    void makeTransfer() {
        Transfer created = service.makeTransfer(user1.getId(), user2.getId(), 100.0);
        TRANSFER_MATCHER.assertMatch(created, transfer1);
        assertEquals(created.getSender().getId(), user1.getId());
        assertEquals(created.getReceiver().getId(), user2.getId());
        BANK_ACCOUNT_MATCHER.assertMatch(bankAccountRepository.getByUserId(USER1_ID), BANK_ACCOUNT_1_AFTER_1_TRANSFER);
        BANK_ACCOUNT_MATCHER.assertMatch(bankAccountRepository.getByUserId(USER1_ID + 1), BANK_ACCOUNT_2_AFTER_1_TRANSFER);
    }

    @Test
    void tranferToMySelf() {
        assertThrows(IllegalArgumentException.class, () -> service.makeTransfer(user1.getId(), user1.getId(), 100.0));
    }

    @Test
    void notEnoughMoney() {
        assertThrows(IllegalArgumentException.class, () -> service.makeTransfer(user1.getId(), user2.getId(), 10000000.0));
    }

    @Test
    void negativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> service.makeTransfer(user1.getId(), user2.getId(), -1.0));
    }
}