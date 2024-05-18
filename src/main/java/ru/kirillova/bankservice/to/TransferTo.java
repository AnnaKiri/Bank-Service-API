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
public class TransferTo extends BaseTo {

    @NotNull(message = "Receiver id is required")
    private Integer receiverId;

    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be positive")
    private Double amount;

    public TransferTo(Integer id, Integer receiverId, Double amount) {
        super(id);
        this.receiverId = receiverId;
        this.amount = amount;
    }
}
