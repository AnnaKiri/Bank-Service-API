package ru.kirillova.bankservice.to;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BankAccountTo extends BaseTo {

    private Double balance;

    public BankAccountTo() {
        super(null);
    }

    public BankAccountTo(Integer id, Double balance) {
        super(id);
        this.balance = balance;
    }
}
