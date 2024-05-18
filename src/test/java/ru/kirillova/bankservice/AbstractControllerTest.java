package ru.kirillova.bankservice;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;
import ru.kirillova.bankservice.model.User;
import ru.kirillova.bankservice.security.JWTProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.kirillova.bankservice.web.user.UserTestData.user1;
import static ru.kirillova.bankservice.web.user.UserTestData.user2;
import static ru.kirillova.bankservice.web.user.UserTestData.user3;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public abstract class AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JWTProvider jwtProvider;

    protected Map<Integer, String> tokens;

    @BeforeEach
    public void filTokensMap() {
        tokens = new HashMap<>();
        for (User user : List.of(user1, user2, user3)) {
            tokens.put(user.getId(), jwtProvider.createToken(user.getUsername()));
        }
    }

    protected ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }
}
