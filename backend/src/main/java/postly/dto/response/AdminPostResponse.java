package postly.dto.response;

import java.time.LocalDateTime;

import postly.entity.PostEntity;

public record AdminPostResponse(
        Long id,
        String title,
        String content,
        UserResponse author,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean isHidden,
        Long likesCount,
        Long commentsCount,
        Long reportsCount) {

    public static AdminPostResponse fromPost(PostEntity post, Long likesCount, Long commentsCount, Long reportsCount) {
        UserResponse author = UserResponse.fromUser(post.getUser()).build();

        return new AdminPostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                author,
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getIsHidden(),
                likesCount,
                commentsCount,
                reportsCount);
    }
}
