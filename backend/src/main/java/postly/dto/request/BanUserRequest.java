package postly.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BanUserRequest(
        @NotBlank(message = "Reason is required") @Size(max = 1000, message = "Reason must not exceed 1000 characters") String reason,

        @Min(value = 0, message = "Duration must be 0 or greater (0 means permanent)") Integer durationDays,

        Boolean permanent) {
}
