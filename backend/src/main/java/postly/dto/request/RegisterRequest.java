package postly.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "First name is required") @Size(min = 3, max = 50, message = "First name must be between 3 and 50 characters") String firstName,

        @NotBlank(message = "Last name is required") @Size(min = 3, max = 50, message = "Last name must be between 3 and 50 characters") String lastName,

        @NotBlank(message = "Username is required") @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters") String username,

        @NotBlank(message = "Email is required") @Email(message = "Email must be valid") @Size(max = 100, message = "Email must not exceed 100 characters") String email,

        @NotBlank(message = "Password is required") @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters") String password,

        @Size(max = 512, message = "Bio must not exceed 512 characters") String bio,

        @Size(max = 512, message = "Profile URL must not exceed 512 characters") String profileUrl) {

}
