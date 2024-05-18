package ru.kirillova.bankservice.web.tranfer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kirillova.bankservice.model.Transfer;
import ru.kirillova.bankservice.repository.TransferRepository;
import ru.kirillova.bankservice.web.AuthUser;

import java.util.List;

@RestController
@RequestMapping(value = ProfileTransferController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class ProfileTransferController {
    static final String REST_URL = "/profile/transfers";

    private TransferRepository transferRepository;

    @GetMapping("/{id}")
    public Transfer get(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        int userId = authUser.getUser().id();
        log.info("get a transfer for user with id {}", userId);
        return transferRepository.getBelonged(userId, id);
    }

    @GetMapping
    public List<Transfer> getAll(@AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.getUser().id();
        log.info("get all transfers for user with id {}", userId);
        return transferRepository.getAllByUser(userId);
    }
}
