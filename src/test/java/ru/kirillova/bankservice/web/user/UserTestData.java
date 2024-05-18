package ru.kirillova.bankservice.web.user;

import ru.kirillova.bankservice.MatcherFactory;
import ru.kirillova.bankservice.model.User;
import ru.kirillova.bankservice.util.JsonUtil;

import java.time.LocalDate;

public class UserTestData {

    public static final int USER1_ID = 1;
    public static final int NOT_FOUND = 100;
    public static final String USER1_MAIL = "user1@example.com";
    public static final String USER2_MAIL = "user2@example.com";
    public static final String USER3_MAIL = "user3@example.com";

    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "bankAccount", "password");

    public static final User user1 = new User(USER1_ID, "user1", "{noop}password1", "+1234567890", USER1_MAIL, LocalDate.of(1990, 1, 1), "User One");
    public static final User user2 = new User(USER1_ID + 1, "user2", "{noop}password2", "+1234567891", USER2_MAIL, LocalDate.of(1991, 1, 1), "User Two");
    public static final User user3 = new User(USER1_ID + 2, "user3", "{noop}password3", "+1234567892", USER3_MAIL, LocalDate.of(1992, 1, 1), "User Three");

    public static User getNew() {
        return new User(null, "NewUser", "newpassword", "+1234567899", "newemail@yandex.ru", LocalDate.of(2000, 1, 1), "New User");
    }

    public static User getUpdated() {
        return new User(USER1_ID, "user1", "{noop}password1", "+999999", "updatedemail@yandex.ru", LocalDate.of(1990, 1, 1), "User One");
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
