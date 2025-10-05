package postly.dto.response;

public record AuthResponse(String accessToken, UserResponse currentUser) {
}