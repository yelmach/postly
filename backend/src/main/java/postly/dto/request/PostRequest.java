package postly.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequest(
                @NotBlank(message = "Title is required") @Size(min = 3, max = 100, message = "Title must not exceed 100 characters") String title,
                @NotBlank(message = "Content is required") String content,
                List<String> mediaUrls) {

}
