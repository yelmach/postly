package postly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import postly.entity.UserEntity;
import postly.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    public UserEntity registerUser(UserEntity user) {
        return userRepository.save(user);
    }

    public UserEntity authenticateUser(UserEntity user) {
        return userRepository.save(user);
    }
}
