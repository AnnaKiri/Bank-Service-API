package ru.kirillova.bankservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.kirillova.bankservice.HasIdAndEmail;
import ru.kirillova.bankservice.HasIdAndPhone;
import ru.kirillova.bankservice.HasUsername;

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
public class User extends AbstractBaseEntity implements HasIdAndEmail, HasIdAndPhone, HasUsername {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private String fullName;

    @ToString.Exclude
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BankAccount bankAccount;

    public User(Integer id, String username, String password, String phone, String email, LocalDate birthDate, String fullName) {
        super(id);
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.birthDate = birthDate;
        this.fullName = fullName;
    }

    public User(String username, String password, String phone, String email, LocalDate birthDate, String fullName) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.birthDate = birthDate;
        this.fullName = fullName;
    }
}
