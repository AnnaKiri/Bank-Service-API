package ru.kirillova.bankservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Table(name = "bank_account")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BankAccount extends AbstractBaseEntity {

    @Column(nullable = false)
    private Double balance;

    @Column(nullable = false)
    @JsonIgnore
    private Double maxBalanceWithInterest;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private User user;

    public BankAccount(Integer id, Double balance, Double maxBalanceWithInterest) {
        super(id);
        this.balance = balance;
        this.maxBalanceWithInterest = maxBalanceWithInterest;
    }

    public BankAccount(Double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", maxBalanceWithInterest=" + maxBalanceWithInterest +
                ", balance=" + balance +
                ", user=" + (user != null ? user.getId() : null) +
                '}';
    }
}
