package ru.kirillova.bankservice.to;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestTo {
    private String username;
    private String password;
}
