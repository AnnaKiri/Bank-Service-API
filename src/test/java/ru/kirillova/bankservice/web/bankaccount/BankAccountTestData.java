package ru.kirillova.bankservice.web.bankaccount;

import ru.kirillova.bankservice.MatcherFactory;
import ru.kirillova.bankservice.model.BankAccount;

public class BankAccountTestData {
    public static final MatcherFactory.Matcher<BankAccount> BANK_ACCOUNT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(BankAccount.class, "user");
    public static final int BANK_ACCOUNT1_ID = 1;
    public static final int NOT_FOUND = 100;

    public static final BankAccount BANK_ACCOUNT_1 = new BankAccount(BANK_ACCOUNT1_ID, 1000.0, 1000.0);
    public static final BankAccount BANK_ACCOUNT_2 = new BankAccount(BANK_ACCOUNT1_ID + 1, 2000.0, 2000.0);
    public static final BankAccount BANK_ACCOUNT_3 = new BankAccount(BANK_ACCOUNT1_ID + 2, 3000.0, 3000.0);

    public static BankAccount getNew() {
        return new BankAccount(null, 9000.0, 9000.0);
    }
}
