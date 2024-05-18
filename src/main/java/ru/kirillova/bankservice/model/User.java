package ru.kirillova.bankservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "phone"),
        @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class User extends AbstractBaseEntity {

    @NotBlank(message = "Username is required")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    @Size(min = 5, max = 128)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number")
    @Column(nullable = false, unique = true)
    private String phone;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date should be in the past")
    @Column(nullable = false)
    private LocalDate birthDate;

    @NotBlank(message = "Full name is required")
    @Column(nullable = false)
    private String fullName;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BankAccount bankAccount;

    public User(Integer id, String username, String password, String phone, String email, LocalDate birthDate, String fullName, BankAccount bankAccount) {
        super(id);
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.birthDate = birthDate;
        this.fullName = fullName;
        this.bankAccount = bankAccount;
    }

    public User(String username, String password, String phone, String email, LocalDate birthDate, String fullName, BankAccount bankAccount) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.birthDate = birthDate;
        this.fullName = fullName;
        this.bankAccount = bankAccount;
    }
}