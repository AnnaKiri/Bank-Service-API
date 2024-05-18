package ru.kirillova.bankservice.web.user;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
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
import ru.kirillova.bankservice.to.UserTo;
import ru.kirillova.bankservice.to.UserUpdateTo;
import ru.kirillova.bankservice.util.UsersUtil;
import ru.kirillova.bankservice.web.AuthUser;

import java.net.URI;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.kirillova.bankservice.validation.ValidationUtil.assureIdConsistent;
import static ru.kirillova.bankservice.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = ProfileController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "Bearer Authentication")
public class ProfileController extends AbstractUserController {
    static final String REST_URL = "/profile";

    private final Logger log = getLogger(getClass());

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    @SecurityRequirement(name = "")
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        log.info("register a new user {}", userTo);
        checkNew(userTo);
        User user = UsersUtil.createNewFromTo(userTo);
        User userCreated = userRepository.prepareAndSaveWithPassword(user);

        BankAccount bankAccount = new BankAccount(userTo.getAmountMoney(), userTo.getAmountMoney(), userCreated);
        BankAccount bankAccountCreated = bankAccountRepository.save(bankAccount);
        userCreated.setBankAccount(bankAccountCreated);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(userCreated);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid UserUpdateTo userUpdateTo, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update the user {} with id={}", userUpdateTo, authUser.id());
        assureIdConsistent(userUpdateTo, authUser.id());
        User updatedUser = UsersUtil.updateFromTo(authUser.getUser(), userUpdateTo);
        userRepository.prepareAndSave(updatedUser);
    }

    @GetMapping
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get the user {}", authUser.getUser().id());
        return authUser.getUser();
    }
}