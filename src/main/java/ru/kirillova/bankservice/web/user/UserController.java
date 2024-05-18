package ru.kirillova.bankservice.web.user;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.kirillova.bankservice.model.BankAccount;
import ru.kirillova.bankservice.model.User;
import ru.kirillova.bankservice.repository.BankAccountRepository;
import ru.kirillova.bankservice.service.UserService;
import ru.kirillova.bankservice.to.UserTo;
import ru.kirillova.bankservice.util.UsersUtil;

import java.net.URI;
import java.time.LocalDate;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.kirillova.bankservice.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = UserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "Bearer Authentication")
@AllArgsConstructor
public class UserController extends AbstractUserController {
    static final String REST_URL = "/users";

    private final Logger log = getLogger(getClass());
    private final BankAccountRepository bankAccountRepository;
    private final UniqueUsernameValidator uniqueUsernameValidator;
    private final UserService userService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        super.initBinder(binder);
        binder.addValidators(uniqueUsernameValidator);
    }

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

    @GetMapping("/search")
    public Page<User> searchUsers(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            Pageable pageable) {

        log.info("Searching users with birthDate={}, phone={}, fullName={}, email={}", birthDate, phone, fullName, email);

        return userService.searchUsers(birthDate, phone, fullName, email, pageable);
    }
}