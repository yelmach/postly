package postly.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import postly.entity.PostEntity;

public record PostResponse(Long id, String title, String content, UserResponse author, LocalDateTime createdAt,
        LocalDateTime updatedAt, List<PostMediaResponse> mediaUrls) {

    public static PostResponse fromPost(PostEntity post) {
        UserResponse author = UserResponse.fromUser(post.getUser()).build();

        List<PostMediaResponse> mediaUrls = post.getMediaFiles().stream()
                .map(postMedia -> PostMediaResponse.fromPostMedia(postMedia))
                .collect(Collectors.toList());

        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                author,
                post.getCreatedAt(),
                post.getUpdatedAt(),
                mediaUrls);
    }
}
