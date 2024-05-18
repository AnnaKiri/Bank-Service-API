package ru.kirillova.bankservice.to;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Integer senderId;

    @NotNull(message = "Receiver id is required")
    private Integer receiverId;

    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be more 0")
    private Double amount;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime timestamp;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String status;

    public TransferTo() {
        super(null);
    }

    public TransferTo(Integer id, Integer senderId, Integer receiverId, Double amount, LocalDateTime timestamp, String status) {
        super(id);
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.timestamp = timestamp;
        this.status = status;
    }

    public TransferTo(Integer id, Integer senderId, Integer receiverId, Double amount) {
        super(id);
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
    }
}
