package postly.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Username or email is required") @Size(min = 3, max = 100, message = "Username or email must be between 3 and 100 characters") String usernameOrEmail,
        @NotBlank(message = "Password is required") @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters") String password) {

}
