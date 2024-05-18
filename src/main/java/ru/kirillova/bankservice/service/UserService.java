package ru.kirillova.bankservice.service;

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
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Page<User> searchUsers(LocalDate birthDate, String phone, String fullName, String email, Pageable pageable) {
        Specification<User> spec = Specification.where(null);

        if (birthDate != null) {
            spec = spec.and(UserSpecifications.hasBirthDateAfter(birthDate));
        }
        if (phone != null) {
            spec = spec.and(UserSpecifications.hasPhone(phone));
        }
        if (fullName != null) {
            spec = spec.and(UserSpecifications.hasFullNameLike(fullName));
        }
        if (email != null) {
            spec = spec.and(UserSpecifications.hasEmail(email));
        }

        return userRepository.findAll(spec, pageable);
    }
}