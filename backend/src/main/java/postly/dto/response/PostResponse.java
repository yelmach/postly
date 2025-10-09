package postly.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import postly.entity.PostEntity;
import postly.entity.PostMediaEntity;

public record PostResponse(Long id, String title, String content, UserResponse author,
        List<PostMediaResponse> mediaFiles, LocalDateTime createdAt, LocalDateTime updatedAt) {

    public static PostResponse fromPost(PostEntity post, List<PostMediaEntity> mediaFiles) {
        UserResponse author = UserResponse.fromUser(post.getUser()).build();

        List<PostMediaResponse> mediaResponses = mediaFiles.stream()
                .map(media -> new PostMediaResponse(
                        media.getId(),
                        media.getMediaUrl(),
                        media.getMediaType(),
                        media.getCreatedAt()))
                .collect(Collectors.toList());

        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                author,
                mediaResponses,
                post.getCreatedAt(),
                post.getUpdatedAt());
    }
}
