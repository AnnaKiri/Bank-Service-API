package ru.kirillova.bankservice.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.kirillova.bankservice.model.User;

import java.time.LocalDate;

@Component
public class UserSpecifications {

    public static Specification<User> hasBirthDateAfter(LocalDate birthDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("birthDate"), birthDate);
    }

    public static Specification<User> hasPhone(String phone) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("phone"), phone);
    }

    public static Specification<User> hasFullNameLike(String fullName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), "%" + fullName.toLowerCase() + "%");
    }

    public static Specification<User> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email);
    }
}
