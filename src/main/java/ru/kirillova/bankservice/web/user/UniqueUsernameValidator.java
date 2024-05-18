package ru.kirillova.bankservice.web.user;

import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import ru.kirillova.bankservice.HasIdAndUsername;
import ru.kirillova.bankservice.repository.UserRepository;

@Component
@AllArgsConstructor
public class UniqueUsernameValidator implements org.springframework.validation.Validator {
    public static final String EXCEPTION_DUPLICATE_USERNAME = "User with this username already exists";

    private final UserRepository repository;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return HasIdAndUsername.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        HasIdAndUsername user = (HasIdAndUsername) target;
        if (StringUtils.hasText(user.getUsername())) {
            repository.findByUsername(user.getUsername())
                    .ifPresent(dbUser -> errors.rejectValue("username", "", EXCEPTION_DUPLICATE_USERNAME));
        }
    }
}
