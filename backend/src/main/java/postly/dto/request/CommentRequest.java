package postly.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotBlank(message = "Content is required") @Size(max = 1000, message = "Content must not exceed 1000 characters") String content) {
}
