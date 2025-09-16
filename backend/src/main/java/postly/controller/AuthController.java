package postly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import postly.entity.Role;
import postly.entity.UserEntity;
import postly.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserEntity> registerUser() {
        UserEntity user = new UserEntity();
        user.setFirstName("yassine");
        user.setLastName("elmach");
        user.setUsername("yelmach");
        user.setEmail("yassine@gmail.com");
        user.setPassword("yassine123");
        user.setRole(Role.ADMIN);
        user.setBio("test user");

        UserEntity userEntity = authService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userEntity);
    }

    @PostMapping("/login")
    public ResponseEntity<UserEntity> LoginUser() {
        UserEntity user = new UserEntity();
        user.setFirstName("yassine");
        user.setLastName("elmach");
        user.setUsername("yelmach");
        user.setEmail("yassine@gmail.com");
        user.setPassword("yassine123");
        user.setRole(Role.ADMIN);
        user.setBio("test user");

        UserEntity userEntity = authService.registerUser(user);
        return ResponseEntity.ok(userEntity);
    }
}
