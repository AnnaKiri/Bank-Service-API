package ru.kirillova.bankservice.to;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TransferTo extends BaseTo {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer senderId;

    @NotNull(message = "Receiver id is required")
    private Integer receiverId;

    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be more 0")
    private Double amount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime timestamp;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String status;

    public TransferTo(Integer id, Integer senderId, Integer receiverId, Double amount, LocalDateTime timestamp, String status) {
        super(id);
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.timestamp = timestamp;
        this.status = status;
    }
}
