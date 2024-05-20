package ru.kirillova.bankservice.web.bankaccount;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kirillova.bankservice.repository.BankAccountRepository;
import ru.kirillova.bankservice.to.BankAccountTo;
import ru.kirillova.bankservice.util.BankAccountUtil;
import ru.kirillova.bankservice.web.AuthUser;

import java.util.List;

@RestController
@RequestMapping(value = ProfileBankAccountController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class ProfileBankAccountController {
    static final String REST_URL = "/profile/bank-accounts";

    private BankAccountRepository bankAccountRepository;

    @GetMapping("/{id}")
    public BankAccountTo get(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        int userId = authUser.getUser().id();
        log.info("get a bank account for user with id {}", userId);
        return BankAccountUtil.createTo(bankAccountRepository.getBelonged(userId, id));
    }

    @GetMapping
    public List<BankAccountTo> getAll(@AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.getUser().id();
        log.info("get all bank accounts for user with id {}", userId);
        return BankAccountUtil.getTos(bankAccountRepository.getAllByUser(userId));
    }
}
