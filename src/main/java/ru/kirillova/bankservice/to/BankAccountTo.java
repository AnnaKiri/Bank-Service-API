package ru.kirillova.bankservice.to;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BankAccountTo extends BaseTo {

    @NotNull(message = "Balance is required")
    @Min(value = 0, message = "Balance must be positive")
    private Double balance;

    public BankAccountTo(Integer id, Double balance) {
        super(id);
        this.balance = balance;
    }
}
