package postly.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import postly.entity.ReportStatus;

public record ResolveReportRequest(
        @NotNull(message = "Status is required") ReportStatus status,

        @Size(max = 1000, message = "Admin notes must not exceed 1000 characters") String adminNotes) {
}
