package postly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
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
    public UserDetails loadUserByUsername(String login) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + login));

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
            throw new DisabledException(banMessage);
        }

        return userEntity;
    }
}
