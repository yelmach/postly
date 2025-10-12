package postly.dto.response;

import postly.entity.MediaType;
import postly.entity.PostMediaEntity;

public record PostMediaResponse(Long id, String mediaUrl, MediaType mediaType) {
    public static PostMediaResponse fromPostMedia(PostMediaEntity postMedia) {

        return new PostMediaResponse(
                postMedia.getId(),
                postMedia.getMediaUrl(),
                postMedia.getMediaType());
    }
}
