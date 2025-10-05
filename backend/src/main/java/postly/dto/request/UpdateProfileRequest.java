package postly.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @Size(min = 3, max = 50, message = "First name must be between 3 and 50 characters") String firstName,

        @Size(min = 3, max = 50, message = "Last name must be between 3 and 50 characters") String lastName,

        @Email(message = "Email must be valid") @Size(max = 100, message = "Email must not exceed 100 characters") String email,

        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters") String password,

        @Size(max = 512, message = "Bio must not exceed 512 characters") String bio) {
}