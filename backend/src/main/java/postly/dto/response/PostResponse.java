package postly.dto.response;

import java.time.LocalDateTime;
import postly.entity.PostEntity;

public record PostResponse(Long id, String title, String content, UserResponse author, LocalDateTime createdAt, LocalDateTime updatedAt) {

    public static PostResponse fromPost(PostEntity post) {
        UserResponse author = UserResponse.fromUser(post.getUser()).build();

        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                author,
                post.getCreatedAt(),
                post.getUpdatedAt());
    }
}
