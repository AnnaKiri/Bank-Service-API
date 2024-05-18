package ru.kirillova.bankservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.kirillova.bankservice.model.User;
import ru.kirillova.bankservice.repository.UserRepository;
import ru.kirillova.bankservice.repository.UserSpecifications;

import java.time.LocalDate;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Page<User> searchUsers(LocalDate birthDate, String phone, String fullName, String email, Pageable pageable) {
        log.info("Searching users with filters - BirthDate: {}, Phone: {}, FullName: {}, Email: {}", birthDate, phone, fullName, email);

        Specification<User> spec = Specification.where(null);

        if (birthDate != null) {
            spec = spec.and(UserSpecifications.hasBirthDateAfter(birthDate));
            log.info("Added filter for birthDate: {}", birthDate);
        }
        if (phone != null) {
            spec = spec.and(UserSpecifications.hasPhone(phone));
            log.info("Added filter for phone: {}", phone);
        }
        if (fullName != null) {
            spec = spec.and(UserSpecifications.hasFullNameLike(fullName));
            log.info("Added filter for fullName: {}", fullName);
        }
        if (email != null) {
            spec = spec.and(UserSpecifications.hasEmail(email));
            log.info("Added filter for email: {}", email);
        }

        Page<User> result = userRepository.findAll(spec, pageable);
        log.info("Found {} users matching the criteria", result.getTotalElements());
        return result;
    }
}
