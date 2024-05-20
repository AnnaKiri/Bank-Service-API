package ru.kirillova.bankservice.web.transfer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.kirillova.bankservice.model.User;
import ru.kirillova.bankservice.repository.BankAccountRepository;
import ru.kirillova.bankservice.repository.TransferRepository;
import ru.kirillova.bankservice.security.JWTProvider;
import ru.kirillova.bankservice.to.TransferTo;
import ru.kirillova.bankservice.util.JsonUtil;
import ru.kirillova.bankservice.util.TransferUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.kirillova.bankservice.BankAccountTestData.BANK_ACCOUNT_1_AFTER_1_TRANSFER;
import static ru.kirillova.bankservice.BankAccountTestData.BANK_ACCOUNT_1_AFTER_2_TRANSFER;
import static ru.kirillova.bankservice.BankAccountTestData.BANK_ACCOUNT_2_AFTER_1_TRANSFER;
import static ru.kirillova.bankservice.BankAccountTestData.BANK_ACCOUNT_3_AFTER_2_TRANSFER;
import static ru.kirillova.bankservice.BankAccountTestData.BANK_ACCOUNT_MATCHER;
import static ru.kirillova.bankservice.TransferTestData.TRANSFER1_ID;
import static ru.kirillova.bankservice.TransferTestData.TRANSFER_MATCHER;
import static ru.kirillova.bankservice.TransferTestData.TRANSFER_TO_MATCHER;
import static ru.kirillova.bankservice.TransferTestData.transfer1;
import static ru.kirillova.bankservice.TransferTestData.transfer2;
import static ru.kirillova.bankservice.TransferTestData.transfer3;
import static ru.kirillova.bankservice.TransferTestData.transfer4;
import static ru.kirillova.bankservice.TransferTestData.transfer5;
import static ru.kirillova.bankservice.UserTestData.USER1_ID;
import static ru.kirillova.bankservice.UserTestData.user1;
import static ru.kirillova.bankservice.UserTestData.user2;
import static ru.kirillova.bankservice.UserTestData.user3;
import static ru.kirillova.bankservice.service.TransferService.INSUFFICIENT_BALANCE_ERROR;
import static ru.kirillova.bankservice.service.TransferService.TRANSFER_TO_YOURSELF_ERROR;
import static ru.kirillova.bankservice.web.transfer.ProfileTransferController.REST_URL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class ProfileTransferControllerTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JWTProvider jwtProvider;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    private Map<Integer, String> tokens;

    private ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }

    @BeforeEach
    public void filTokensMap() {
        tokens = new HashMap<>();
        for (User user : List.of(user1, user2, user3)) {
            tokens.put(user.getId(), jwtProvider.createToken(user.getUsername()));
        }
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TRANSFER1_ID)
                .header("Authorization", "Bearer " + tokens.get(user1.getId())))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRANSFER_TO_MATCHER.contentJson(TransferUtil.createTo(transfer1)));
    }

    @Test
    void getAllUser1() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .header("Authorization", "Bearer " + tokens.get(user1.getId())))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRANSFER_TO_MATCHER.contentJson(TransferUtil.createTo(transfer1), TransferUtil.createTo(transfer5)));
    }

    @Test
    void getAllUser2() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .header("Authorization", "Bearer " + tokens.get(user2.getId())))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRANSFER_TO_MATCHER.contentJson(TransferUtil.createTo(transfer3), TransferUtil.createTo(transfer4)));
    }

    @Test
    @DirtiesContext
    void makeTransferUser1ToUser2() throws Exception {
        TransferTo newTo = new TransferTo(null, null, user2.getId(), 100.0);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .header("Authorization", "Bearer " + tokens.get(user1.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        TransferTo created = TRANSFER_TO_MATCHER.readFromJson(action);
        TRANSFER_TO_MATCHER.assertMatch(created, TransferUtil.createTo(transfer1));
        TRANSFER_MATCHER.assertMatch(transferRepository.getBelonged(user1.getId(), created.getId()), transfer1);

        BANK_ACCOUNT_MATCHER.assertMatch(bankAccountRepository.getByUserId(USER1_ID), BANK_ACCOUNT_1_AFTER_1_TRANSFER);
        BANK_ACCOUNT_MATCHER.assertMatch(bankAccountRepository.getByUserId(USER1_ID + 1), BANK_ACCOUNT_2_AFTER_1_TRANSFER);
    }

    @Test
    @DirtiesContext
    void makeTransferUser3ToUser1() throws Exception {
        TransferTo newTo = new TransferTo(null, null, user1.getId(), 200.0);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .header("Authorization", "Bearer " + tokens.get(user3.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        TransferTo created = TRANSFER_TO_MATCHER.readFromJson(action);
        TRANSFER_TO_MATCHER.assertMatch(created, TransferUtil.createTo(transfer2));
        TRANSFER_MATCHER.assertMatch(transferRepository.getBelonged(user3.getId(), created.getId()), transfer2);

        BANK_ACCOUNT_MATCHER.assertMatch(bankAccountRepository.getByUserId(USER1_ID + 2), BANK_ACCOUNT_3_AFTER_2_TRANSFER);
        BANK_ACCOUNT_MATCHER.assertMatch(bankAccountRepository.getByUserId(USER1_ID), BANK_ACCOUNT_1_AFTER_2_TRANSFER);
    }

    @Test
    @DirtiesContext
    void makeTransferUser2ToUser3() throws Exception {
        TransferTo newTo = new TransferTo(null, null, user3.getId(), 300.0);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .header("Authorization", "Bearer " + tokens.get(user2.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        TransferTo created = TRANSFER_TO_MATCHER.readFromJson(action);
        TRANSFER_TO_MATCHER.assertMatch(created, TransferUtil.createTo(transfer3));
        TRANSFER_MATCHER.assertMatch(transferRepository.getBelonged(user2.getId(), created.getId()), transfer3);
    }

    @Test
    @DirtiesContext
    void makeTransferToMyself() throws Exception {
        TransferTo newTo = new TransferTo(null, null, user2.getId(), 100.0);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .header("Authorization", "Bearer " + tokens.get(user2.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(TRANSFER_TO_YOURSELF_ERROR)));
        ;
    }

    @Test
    @DirtiesContext
    void makeTransferWithInsufficientMoney() throws Exception {
        TransferTo newTo = new TransferTo(null, null, user2.getId(), 100000.0);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .header("Authorization", "Bearer " + tokens.get(user1.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(INSUFFICIENT_BALANCE_ERROR)));
    }

    @Test
    void makeTransferWithNegativeAmount() throws Exception {
        TransferTo newTo = new TransferTo(null, null, user1.getId(), -100.0);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .header("Authorization", "Bearer " + tokens.get(user3.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("Amount must be more 0")));
    }

    @Test
    void makeTransferWithoutReceiver() throws Exception {
        TransferTo newTo = new TransferTo(null, null, null, 100.0);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .header("Authorization", "Bearer " + tokens.get(user3.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("Receiver id is required")));
    }

    @Test
    void makeTransferWithNullAmount() throws Exception {
        TransferTo newTo = new TransferTo(null, null, user1.getId(), null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .header("Authorization", "Bearer " + tokens.get(user3.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("Amount is required")));
    }

}