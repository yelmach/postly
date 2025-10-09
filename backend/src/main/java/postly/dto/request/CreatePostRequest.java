package postly.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePostRequest(
        @NotBlank(message = "Title is required") @Size(max = 100, message = "Title must not exceed 100 characters") String title,

        @NotBlank(message = "Content is required") String content,

        List<MultipartFile> files) {
}
