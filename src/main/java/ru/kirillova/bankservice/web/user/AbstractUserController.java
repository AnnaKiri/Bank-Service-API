package ru.kirillova.bankservice.web.user;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.kirillova.bankservice.repository.UserRepository;
import ru.kirillova.bankservice.service.UserService;

import static org.slf4j.LoggerFactory.getLogger;

public class AbstractUserController {
    protected final Logger log = getLogger(getClass());

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected UserService userService;
    @Autowired
    private UniqueMailValidator emailValidator;
    @Autowired
    private PhoneOrEmailPresenceValidator phoneOrEmailPresenceValidator;
    @Autowired
    private UniquePhoneValidator phoneValidator;
    @Autowired
    private UniqueUsernameValidator uniqueUsernameValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
        binder.addValidators(phoneOrEmailPresenceValidator);
        binder.addValidators(phoneValidator);
        binder.addValidators(uniqueUsernameValidator);
    }
}
