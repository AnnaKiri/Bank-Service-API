package ru.kirillova.bankservice;

import ru.kirillova.bankservice.model.Transfer;
import ru.kirillova.bankservice.to.TransferTo;

import java.time.LocalDateTime;

import static ru.kirillova.bankservice.UserTestData.user1;
import static ru.kirillova.bankservice.UserTestData.user2;
import static ru.kirillova.bankservice.UserTestData.user3;


public class TransferTestData {

    public static final int TRANSFER1_ID = 1;
    public static final int NOT_FOUND = 100;

    public static final MatcherFactory.Matcher<Transfer> TRANSFER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Transfer.class, "id", "timestamp", "sender", "receiver");
    public static final MatcherFactory.Matcher<TransferTo> TRANSFER_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(TransferTo.class, "id", "timestamp");

    public static final Transfer transfer1 = new Transfer(TRANSFER1_ID, user1, user2, 100.0, LocalDateTime.now(), "SUCCESS");
    public static final Transfer transfer2 = new Transfer(TRANSFER1_ID + 1, user3, user1, 200.0, LocalDateTime.now(), "SUCCESS");
    public static final Transfer transfer3 = new Transfer(TRANSFER1_ID + 2, user2, user3, 300.0, LocalDateTime.now(), "SUCCESS");
    public static final Transfer transfer4 = new Transfer(TRANSFER1_ID + 3, user2, user2, 100.0, LocalDateTime.now(), "FAIL");
    public static final Transfer transfer5 = new Transfer(TRANSFER1_ID + 4, user1, user2, 100000.0, LocalDateTime.now(), "FAIL");
    public static final Transfer transfer6 = new Transfer(TRANSFER1_ID + 5, user3, user1, -100.0, LocalDateTime.now(), "FAIL");


    public static TransferTo getNew() {
        return new TransferTo(null, user2.getId(), user1.getId(), 50.0);
    }
}
