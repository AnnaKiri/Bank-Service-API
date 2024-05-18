package ru.kirillova.bankservice.web.tranfer;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.kirillova.bankservice.model.Transfer;
import ru.kirillova.bankservice.repository.TransferRepository;
import ru.kirillova.bankservice.repository.UserRepository;
import ru.kirillova.bankservice.service.TransferService;
import ru.kirillova.bankservice.to.TransferTo;
import ru.kirillova.bankservice.web.AuthUser;

import java.net.URI;
import java.util.List;

import static ru.kirillova.bankservice.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = ProfileTransferController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class ProfileTransferController {
    static final String REST_URL = "/profile/transfers";

    private TransferService transferService;
    private TransferRepository transferRepository;
    private UserRepository userRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Transfer> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody TransferTo transferTo) {
        checkNew(transferTo);

        Integer senderId = authUser.getUser().id();
        Integer receiverId = transferTo.getReceiverId();
        Double amount = transferTo.getAmount();
        log.info("create a transfer from the user id {} to user id {} with amount {}", senderId, receiverId, amount);
        userRepository.checkExisted(receiverId);

        Transfer created = transferService.makeTransfer(senderId, receiverId, amount);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

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
