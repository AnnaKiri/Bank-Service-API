package ru.kirillova.bankservice;

public interface HasIdAndEmailAndPhone extends HasId {
    String getPhone();

    String getEmail();
}
