package postly.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import postly.entity.ReportReason;

public record ReportRequest(
                @NotNull(message = "Reason is required") ReportReason reason,

                @Size(max = 1000, message = "Description must not exceed 1000 characters") String description) {
}
