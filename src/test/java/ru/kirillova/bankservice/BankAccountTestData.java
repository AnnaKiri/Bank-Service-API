package ru.kirillova.bankservice;

import ru.kirillova.bankservice.model.BankAccount;

public class BankAccountTestData {
    public static final MatcherFactory.Matcher<BankAccount> BANK_ACCOUNT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(BankAccount.class, "user");
    public static final int BANK_ACCOUNT1_ID = 1;
    public static final int NOT_FOUND = 100;

    public static final BankAccount BANK_ACCOUNT_1_BEFORE_TRANSFERS = new BankAccount(BANK_ACCOUNT1_ID, 1000.0, 1100.0);
    public static final BankAccount BANK_ACCOUNT_2_BEFORE_TRANSFERS = new BankAccount(BANK_ACCOUNT1_ID + 1, 2000.0, 1800.0);
    public static final BankAccount BANK_ACCOUNT_3_BEFORE_TRANSFERS = new BankAccount(BANK_ACCOUNT1_ID + 2, 3000.0, 3100.0);

    public static final BankAccount BANK_ACCOUNT_1_AFTER_1_TRANSFER = new BankAccount(BANK_ACCOUNT1_ID, 1000.0, 1000.0);
    public static final BankAccount BANK_ACCOUNT_2_AFTER_1_TRANSFER = new BankAccount(BANK_ACCOUNT1_ID + 1, 2000.0, 1900.0);

    public static final BankAccount BANK_ACCOUNT_3_AFTER_2_TRANSFER = new BankAccount(BANK_ACCOUNT1_ID + 2, 3000.0, 2900.0);
    public static final BankAccount BANK_ACCOUNT_1_AFTER_2_TRANSFER = new BankAccount(BANK_ACCOUNT1_ID, 1000.0, 1300.0);

    public static BankAccount getNew() {
        return new BankAccount(null, 9000.0, 9000.0);
    }
}
