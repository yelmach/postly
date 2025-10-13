package postly.dto.response;

import java.time.LocalDateTime;

import postly.entity.CommentEntity;

public record CommentResponse(Long id, String content, UserResponse author, LocalDateTime createdAt) {

    public static CommentResponse fromComment(CommentEntity comment) {
        UserResponse author = UserResponse.fromUser(comment.getUser()).build();

        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                author,
                comment.getCreatedAt());
    }
}
