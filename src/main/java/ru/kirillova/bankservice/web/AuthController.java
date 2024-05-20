package ru.kirillova.bankservice.web;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.kirillova.bankservice.security.JWTProvider;
import ru.kirillova.bankservice.to.AuthResponseTo;
import ru.kirillova.bankservice.to.LoginRequestTo;

@RestController
@Slf4j
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JWTProvider jwtProvider;

    @PostMapping("/login")
    public AuthResponseTo authenticate(@RequestBody LoginRequestTo loginRequest) {
        log.info("Attempt to login by user: {}", loginRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.createToken(loginRequest.getUsername());
        log.info("Login successful for user: {}", loginRequest.getUsername());
        return new AuthResponseTo(jwt);

    }
}
