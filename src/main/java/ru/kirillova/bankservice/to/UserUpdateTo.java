package ru.kirillova.bankservice.to;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.kirillova.bankservice.HasIdAndEmail;
import ru.kirillova.bankservice.HasIdAndPhone;
import ru.kirillova.bankservice.validation.NoHtml;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserUpdateTo extends BaseTo implements HasIdAndEmail, HasIdAndPhone {

    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Invalid phone number")
    private String phone;

    @Email(message = "Email should be valid")
    @NoHtml
    private String email;

    public UserUpdateTo() {
        super(null);
    }

    public UserUpdateTo(Integer id, String phone, String email) {
        super(id);
        this.phone = phone;
        this.email = email;
    }
}
