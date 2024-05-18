package ru.kirillova.bankservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

    @Column(nullable = false)
    private Double initialBalance;

    @Column(nullable = false)
    private Double balance;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private User user;

    public BankAccount(Integer id, Double initialBalance, Double balance) {
        super(id);
        this.initialBalance = initialBalance;
        this.balance = balance;
    }

    public BankAccount(Double initialBalance, Double balance) {
        this.initialBalance = initialBalance;
        this.balance = balance;
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
