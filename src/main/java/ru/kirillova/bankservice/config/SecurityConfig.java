package ru.kirillova.bankservice.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.kirillova.bankservice.model.User;
import ru.kirillova.bankservice.repository.UserRepository;
import ru.kirillova.bankservice.security.JWTProvider;
import ru.kirillova.bankservice.security.JWTTokenFilter;
import ru.kirillova.bankservice.web.AuthUser;

import java.util.Optional;

@Configuration
@EnableWebSecurity
@Slf4j
@AllArgsConstructor
public class SecurityConfig {
    public static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final UserRepository userRepository;
    private final JWTProvider jwtProvider;

    @Bean
    PasswordEncoder passwordEncoder() {
        return PASSWORD_ENCODER;
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> {
            log.debug("Authenticating '{}'", username);
            Optional<User> optionalUser = userRepository.findByUsername(username);
            return new AuthUser(optionalUser.orElseThrow(
                    () -> new UsernameNotFoundException("User '" + username + "' was not found")));
        };
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager, UserDetailsService userDetailsService) throws Exception {
        http.authenticationManager(authenticationManager)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/users").anonymous()
                        .requestMatchers(HttpMethod.POST, "/login").anonymous()
                        .requestMatchers("/", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JWTTokenFilter(jwtProvider, userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
