package ru.kirillova.bankservice.web.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import ru.kirillova.bankservice.HasIdAndPhone;
import ru.kirillova.bankservice.repository.UserRepository;
import ru.kirillova.bankservice.web.AuthUser;


@Component
@AllArgsConstructor
public class UniquePhoneValidator implements org.springframework.validation.Validator {
    public static final String EXCEPTION_DUPLICATE_PHONE = "User with this phone already exists";

    private final UserRepository repository;
    private final HttpServletRequest request;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return HasIdAndPhone.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        HasIdAndPhone user = (HasIdAndPhone) target;
        if (StringUtils.hasText(user.getPhone())) {
            repository.findByPhone(user.getPhone())
                    .ifPresent(dbUser -> {
                        if (request.getMethod().equals("PUT")) {
                            int dbId = dbUser.id();

                            // it is ok, if update ourselves
                            if (user.getId() != null && dbId == user.id()) return;

                            // Workaround for update with user.id=null in request body
                            // ValidationUtil.assureIdConsistent called after this validation
                            String requestURI = request.getRequestURI();
                            if (requestURI.endsWith("/" + dbId) || (dbId == AuthUser.authId() && requestURI.contains("/profile")))
                                return;
                        }
                        errors.rejectValue("phone", "", EXCEPTION_DUPLICATE_PHONE);
                    });
        }
    }
}
