package postly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import postly.dto.request.CommentRequest;
import postly.dto.response.CommentResponse;
import postly.entity.CommentEntity;
import postly.entity.PostEntity;
import postly.entity.UserEntity;
import postly.exception.ApiException;
import postly.repository.CommentRepository;
import postly.repository.PostRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public CommentResponse createComment(Long postId, CommentRequest request) {
        UserEntity currentUser = userService.getCurrentUserEntity();
        PostEntity post = postRepository.findById(postId).orElseThrow(() -> ApiException.notFound("Post not found"));

        CommentEntity comment = new CommentEntity(post, currentUser, request.content());
        comment = commentRepository.save(comment);

        return CommentResponse.fromComment(comment);
    }

    public Page<CommentResponse> getCommentsByPost(Long postId, Pageable pageable) {
        if (!postRepository.existsById(postId)) {
            throw ApiException.notFound("Post not found");
        }

        Page<CommentEntity> comments = commentRepository.findByPostIdOrderByCreatedAtDesc(postId, pageable);
        return comments.map(CommentResponse::fromComment);
    }

    public long countCommentsByPost(Long postId) {
        return commentRepository.countByPostId(postId);
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, CommentRequest request) {
        UserEntity currentUser = userService.getCurrentUserEntity();
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> ApiException.notFound("Comment not found"));

        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw ApiException.forbidden("You are not authorized to update this comment");
        }

        comment.setContent(request.content());
        comment = commentRepository.save(comment);

        return CommentResponse.fromComment(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        UserEntity currentUser = userService.getCurrentUserEntity();
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> ApiException.notFound("Comment not found"));

        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw ApiException.forbidden("You are not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }
}
