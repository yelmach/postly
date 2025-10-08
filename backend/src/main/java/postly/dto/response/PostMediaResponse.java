package postly.dto.response;

import java.time.LocalDateTime;

import postly.entity.MediaType;

public class PostMediaResponse {

    private Long id;
    private String url;
    private MediaType type;
    private Integer displayOrder;
    private LocalDateTime createdAt;

    public PostMediaResponse() {
    }

    public PostMediaResponse(Long id, String url, MediaType type, Integer displayOrder, LocalDateTime createdAt) {
        this.id = id;
        this.url = url;
        this.type = type;
        this.displayOrder = displayOrder;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
