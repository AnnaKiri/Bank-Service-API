package ru.kirillova.bankservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bank_accounts")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BankAccount extends AbstractBaseEntity {

    @NotNull(message = "Initial balance is required")
    @Column(nullable = false)
    private Double initialBalance;

    @NotNull(message = "Balance is required")
    @Column(nullable = false)
    private Double balance;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public BankAccount(Integer id, Double initialBalance, Double balance, User user) {
        super(id);
        this.initialBalance = initialBalance;
        this.balance = balance;
        this.user = user;
    }

    public BankAccount(Double initialBalance, Double balance, User user) {
        this.initialBalance = initialBalance;
        this.balance = balance;
        this.user = user;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", initialBalance=" + initialBalance +
                ", balance=" + balance +
                ", user=" + user.getId() +
                '}';
    }
}
