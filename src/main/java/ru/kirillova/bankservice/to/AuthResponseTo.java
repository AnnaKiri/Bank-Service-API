package ru.kirillova.bankservice.to;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AuthResponseTo {
    private String token;
}
