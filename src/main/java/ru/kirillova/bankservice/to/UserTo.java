package ru.kirillova.bankservice.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.kirillova.bankservice.HasIdAndEmail;
import ru.kirillova.bankservice.HasIdAndPhone;
import ru.kirillova.bankservice.HasUsername;
import ru.kirillova.bankservice.validation.NoHtml;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserTo extends BaseTo implements HasIdAndEmail, HasIdAndPhone, HasUsername {

    @NotBlank(message = "Username is required")
    @NoHtml
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 5, max = 128)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Invalid phone number")
    private String phone;

    @Email(message = "Email should be valid")
    @NoHtml
    private String email;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date should be in the past")
    private LocalDate birthDate;

    @NotBlank(message = "Full name is required")
    @NoHtml
    private String fullName;

    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY)
    private Double amountMoney;

    public UserTo() {
        super(null);
    }

    public UserTo(Integer id, String username, String password, String phone, String email, LocalDate birthDate, String fullName, Double amountMoney) {
        super(id);
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.birthDate = birthDate;
        this.fullName = fullName;
        this.amountMoney = amountMoney;
    }
}
