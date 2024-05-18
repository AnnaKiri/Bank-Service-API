package ru.kirillova.bankservice.web.user;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.kirillova.bankservice.model.BankAccount;
import ru.kirillova.bankservice.model.User;
import ru.kirillova.bankservice.repository.BankAccountRepository;
import ru.kirillova.bankservice.repository.UserRepository;
import ru.kirillova.bankservice.to.UserTo;
import ru.kirillova.bankservice.util.UsersUtil;
import ru.kirillova.bankservice.web.AuthUser;

import java.net.URI;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.kirillova.bankservice.validation.ValidationUtil.assureIdConsistent;
import static ru.kirillova.bankservice.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = ProfileController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController {
    static final String REST_URL = "/profile";

    private final Logger log = getLogger(getClass());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UniqueMailValidator emailValidator;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private PhoneOrEmailPresenceValidator phoneOrEmailPresenceValidator;
    @Autowired
    private UniquePhoneValidator phoneValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
        binder.addValidators(phoneOrEmailPresenceValidator);
        binder.addValidators(phoneValidator);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        log.info("register a new user {}", user);
        checkNew(user);
        User created = userRepository.prepareAndSave(user);
        BankAccount bankAccount = user.getBankAccount();
        bankAccount.setUser(created);
        bankAccountRepository.save(bankAccount);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid UserTo userTo, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update the user {} with id={}", userTo, authUser.id());
        assureIdConsistent(userTo, authUser.id());
        User updatedUser = UsersUtil.updateFromTo(authUser.getUser(), userTo);
        userRepository.prepareAndSave(updatedUser);
    }

    @GetMapping
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get the user {}", authUser.getUser().id());
        return authUser.getUser();
    }
}