package postly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import postly.entity.LikeEntity;
import postly.entity.PostEntity;
import postly.entity.UserEntity;
import postly.exception.ApiException;
import postly.repository.LikeRepository;
import postly.repository.PostRepository;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public boolean toggleLike(Long postId) {
        UserEntity currentUser = userService.getCurrentUserEntity();
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> ApiException.notFound("Post not found"));

        boolean exists = likeRepository.existsByUserIdAndPostId(currentUser.getId(), postId);

        if (exists) {
            likeRepository.deleteByUserIdAndPostId(currentUser.getId(), postId);
            return false;
        } else {
            try {
                LikeEntity like = new LikeEntity(currentUser, post);
                likeRepository.save(like);
                return true;
            } catch (DataIntegrityViolationException e) {
                likeRepository.deleteByUserIdAndPostId(currentUser.getId(), postId);
                return false;
            }
        }
    }

    public long countLikesByPost(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    public boolean isPostLikedByUser(Long postId, Long userId) {
        return likeRepository.existsByUserIdAndPostId(userId, postId);
    }
}
