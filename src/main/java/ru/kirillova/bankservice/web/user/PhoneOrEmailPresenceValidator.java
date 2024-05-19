package ru.kirillova.bankservice.web.user;

import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import ru.kirillova.bankservice.HasIdAndEmail;
import ru.kirillova.bankservice.HasIdAndPhone;

@Component
@AllArgsConstructor
public class PhoneOrEmailPresenceValidator implements org.springframework.validation.Validator {
    public static final String EXCEPTION_NO_PHONE_AND_EMAIL = "Profile must have at least information about phone or email";

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return HasIdAndEmail.class.isAssignableFrom(clazz) && HasIdAndPhone.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        HasIdAndEmail userWithEmail = (HasIdAndEmail) target;
        HasIdAndPhone userWithPhone = (HasIdAndPhone) target;
        if (!StringUtils.hasText(userWithPhone.getPhone()) && !StringUtils.hasText(userWithEmail.getEmail())) {
            errors.reject("noPhoneAndEmail", EXCEPTION_NO_PHONE_AND_EMAIL);
        }
    }
}
