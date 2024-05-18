package ru.kirillova.bankservice.util;

import lombok.experimental.UtilityClass;
import ru.kirillova.bankservice.model.User;
import ru.kirillova.bankservice.to.UserTo;

@UtilityClass
public class UsersUtil {

    public static User updateFromTo(User user, UserTo userTo) {
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPhone(userTo.getPhone());
        return user;
    }
}