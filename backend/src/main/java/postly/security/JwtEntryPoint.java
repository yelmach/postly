package postly.security;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import postly.dto.response.ErrorResponse;

@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String errorMessage = "Authentication required to access this resource";

        Object jwtError = request.getAttribute("jwt_error");
        if (jwtError != null) {
            errorMessage = jwtError.toString();
        } else if (authException.getMessage() != null) {
            if (authException.getMessage().contains("expired")) {
                errorMessage = "Token has expired. Please login again.";
            } else if (authException.getMessage().contains("malformed")) {
                errorMessage = "Invalid token format";
            } else if (authException.getMessage().contains("signature")) {
                errorMessage = "Token signature verification failed";
            }
        }

        ErrorResponse err = new ErrorResponse(401, "Unauthorized", errorMessage);

        String jsonResponse = objectMapper.writeValueAsString(err);
        response.getWriter().write(jsonResponse);
    }
}
