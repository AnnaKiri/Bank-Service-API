package ru.kirillova.bankservice.util;

import lombok.experimental.UtilityClass;
import ru.kirillova.bankservice.model.User;
import ru.kirillova.bankservice.to.UserTo;
import ru.kirillova.bankservice.to.UserUpdateTo;

@UtilityClass
public class UsersUtil {

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getUsername(), userTo.getPassword(), userTo.getPhone(), userTo.getEmail().toLowerCase(), userTo.getBirthDate(), userTo.getFullName());
    }

    public static User updateFromTo(User user, UserUpdateTo userUpdateTo) {
        user.setEmail(userUpdateTo.getEmail() != null ? userUpdateTo.getEmail().toLowerCase() : null);
        user.setPhone(userUpdateTo.getPhone() != null ? userUpdateTo.getPhone() : null);
        return user;
    }
}