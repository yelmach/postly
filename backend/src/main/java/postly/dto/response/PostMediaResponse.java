package postly.dto.response;

import postly.entity.MediaType;

public record PostMediaResponse(Long id, String mediaUrl, MediaType mediaType) {
}
