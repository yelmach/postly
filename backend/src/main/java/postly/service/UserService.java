package postly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import postly.dto.response.UserResponse;
import postly.entity.UserEntity;
import postly.exception.ApiException;
import postly.repository.PostRepository;
import postly.repository.SubscriptionRepository;
import postly.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    public UserResponse getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) auth.getPrincipal();

        long postsCount = postRepository.countByUserId(currentUser.getId());
        long followersCount = subscriptionRepository.countBySubscribedToId(currentUser.getId());
        long followingCount = subscriptionRepository.countBySubscriberId(currentUser.getId());

        return UserResponse.fromUser(currentUser)
                .postsCount(postsCount)
                .followersCount(followersCount)
                .followingCount(followingCount)
                .build();
    }

    public UserResponse getUserByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> ApiException.notFound("User not exist with username: " + username));

        long postsCount = postRepository.countByUserId(user.getId());
        long followersCount = subscriptionRepository.countBySubscribedToId(user.getId());
        long followingCount = subscriptionRepository.countBySubscriberId(user.getId());

        return UserResponse.fromUser(user)
                .postsCount(postsCount)
                .followersCount(followersCount)
                .followingCount(followingCount)
                .build();
    }
}
