package ru.kirillova.bankservice.web.transfer;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.kirillova.bankservice.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.kirillova.bankservice.web.transfer.ProfileTransferController.REST_URL;
import static ru.kirillova.bankservice.web.transfer.TransferTestData.TRANSFER1_ID;
import static ru.kirillova.bankservice.web.transfer.TransferTestData.TRANSFER_TO_MATCHER;
import static ru.kirillova.bankservice.web.transfer.TransferTestData.transfer1;
import static ru.kirillova.bankservice.web.user.UserTestData.user1;

class ProfileTransferControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + '/';

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TRANSFER1_ID)
                .header("Authorization", "Bearer " + tokens.get(user1.getId())))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TRANSFER_TO_MATCHER.contentJson(transfer1));
    }

}