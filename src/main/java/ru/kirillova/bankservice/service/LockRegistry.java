package ru.kirillova.bankservice.service;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class LockRegistry {

    private final ConcurrentHashMap<Integer, Object> lockMap = new ConcurrentHashMap<>();

    public Object getLock(Integer accountId) {
        return lockMap.computeIfAbsent(accountId, id -> new Object());
    }

}
