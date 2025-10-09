package postly.dto.response;

import java.time.LocalDateTime;

import postly.entity.MediaType;

public record PostMediaResponse(Long id, String url, MediaType type, LocalDateTime createdAt) {
}
