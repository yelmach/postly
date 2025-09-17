package postly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import postly.dto.request.LoginRequest;
import postly.dto.request.RegisterRequest;
import postly.dto.response.AuthResponse;
import postly.dto.response.UserResponse;
import postly.entity.UserEntity;
import postly.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        String jwtToken = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(jwtToken));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        String jwtToken = authService.login(loginRequest);
        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) auth.getPrincipal();

        UserResponse userResponse = UserResponse.fromUser(currentUser).build();

        return ResponseEntity.ok(userResponse);
    }
}
