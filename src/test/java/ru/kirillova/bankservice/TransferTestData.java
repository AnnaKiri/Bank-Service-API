package ru.kirillova.bankservice;

import ru.kirillova.bankservice.to.TransferTo;

import java.time.LocalDateTime;

import static ru.kirillova.bankservice.UserTestData.user1;
import static ru.kirillova.bankservice.UserTestData.user2;
import static ru.kirillova.bankservice.UserTestData.user3;


public class TransferTestData {

    public static final int TRANSFER1_ID = 1;
    public static final int NOT_FOUND = 100;

    public static final MatcherFactory.Matcher<TransferTo> TRANSFER_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(TransferTo.class, "timestamp");

    public static final TransferTo transfer1 = new TransferTo(TRANSFER1_ID, user1.getId(), user2.getId(), 100.0, LocalDateTime.now(), "SUCCESS");
    public static final TransferTo transfer2 = new TransferTo(TRANSFER1_ID + 1, user3.getId(), user1.getId(), 200.0, LocalDateTime.now(), "SUCCESS");
    public static final TransferTo transfer3 = new TransferTo(TRANSFER1_ID + 2, user2.getId(), user3.getId(), 300.0, LocalDateTime.now(), "SUCCESS");
    public static final TransferTo transfer4 = new TransferTo(TRANSFER1_ID + 3, user2.getId(), user2.getId(), 100.0, LocalDateTime.now(), "FAIL");
    public static final TransferTo transfer5 = new TransferTo(TRANSFER1_ID + 4, user1.getId(), user2.getId(), 100000.0, LocalDateTime.now(), "FAIL");


    public static TransferTo getNew() {
        return new TransferTo(null, user2.getId(), user1.getId(), 50.0);
    }
}
