package ru.kirillova.bankservice.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.kirillova.bankservice.error.NotFoundException;
import ru.kirillova.bankservice.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static ru.kirillova.bankservice.config.SecurityConfig.PASSWORD_ENCODER;

@Transactional(readOnly = true)
public interface UserRepository extends BaseRepository<User>, JpaSpecificationExecutor<User> {
    @Query("SELECT u FROM User u WHERE u.email = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(String email);

    default User getExistedByEmail(String email) {
        return findByEmailIgnoreCase(email).orElseThrow(() -> new NotFoundException("User with email=" + email + " not found"));
    }

    @Transactional
    default User prepareAndSave(User user) {
        user.setEmail(user.getEmail().toLowerCase());
        return save(user);
    }

    default User prepareAndSaveWithPassword(User user) {
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        return prepareAndSave(user);
    }

    default void checkExisted(int userId) {
        if (!existsById(userId)) {
            throw new NotFoundException("User id=" + userId + " does not exist");
        }
    }

    Optional<User> findByUsername(String username);

    Optional<User> findByPhone(String phone);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.birthDate > :birthday")
    Optional<User> findByBirthday(LocalDate birthday);
}