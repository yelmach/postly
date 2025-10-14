package postly.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import postly.entity.UserEntity;
import postly.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByUsernameOrEmail(login);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username or email: " + login);
        }

        UserEntity userEntity = user.get();

        if (userEntity.isActiveBan()) {
            String banMessage = "Your account has been banned";
            if (userEntity.getBannedUntil() != null) {
                banMessage += " until " + userEntity.getBannedUntil();
            } else {
                banMessage += " permanently";
            }
            if (userEntity.getBanReason() != null) {
                banMessage += ". Reason: " + userEntity.getBanReason();
            }
            throw new UsernameNotFoundException(banMessage);
        }

        return userEntity;
    }
}
