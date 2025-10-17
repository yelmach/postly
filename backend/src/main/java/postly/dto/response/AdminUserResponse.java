package postly.dto.response;

import java.time.LocalDateTime;

import postly.entity.Role;
import postly.entity.UserEntity;

public class AdminUserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Role role;
    private String bio;
    private String profileUrl;
    private LocalDateTime createdAt;
    private Boolean isBanned;
    private LocalDateTime bannedUntil;
    private String banReason;
    private Long postsCount;
    private Long subscribersCount;
    private Long subscriptionsCount;
    private Long reportsCount;

    private AdminUserResponse(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.username = builder.username;
        this.email = builder.email;
        this.role = builder.role;
        this.bio = builder.bio;
        this.profileUrl = builder.profileUrl;
        this.createdAt = builder.createdAt;
        this.isBanned = builder.isBanned;
        this.bannedUntil = builder.bannedUntil;
        this.banReason = builder.banReason;
        this.postsCount = builder.postsCount;
        this.subscribersCount = builder.subscribersCount;
        this.subscriptionsCount = builder.subscriptionsCount;
        this.reportsCount = builder.reportsCount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder fromUser(UserEntity user) {
        return new Builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .bio(user.getBio())
                .profileUrl(user.getProfileUrl())
                .createdAt(user.getCreatedAt())
                .isBanned(user.getIsBanned())
                .bannedUntil(user.getBannedUntil())
                .banReason(user.getBanReason());
    }

    public static class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private String username;
        private String email;
        private Role role;
        private String bio;
        private String profileUrl;
        private LocalDateTime createdAt;
        private Boolean isBanned;
        private LocalDateTime bannedUntil;
        private String banReason;
        private Long postsCount;
        private Long subscribersCount;
        private Long subscriptionsCount;
        private Long reportsCount;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public Builder profileUrl(String profileUrl) {
            this.profileUrl = profileUrl;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder isBanned(Boolean isBanned) {
            this.isBanned = isBanned;
            return this;
        }

        public Builder bannedUntil(LocalDateTime bannedUntil) {
            this.bannedUntil = bannedUntil;
            return this;
        }

        public Builder banReason(String banReason) {
            this.banReason = banReason;
            return this;
        }

        public Builder postsCount(Long postsCount) {
            this.postsCount = postsCount;
            return this;
        }

        public Builder subscribersCount(Long subscribersCount) {
            this.subscribersCount = subscribersCount;
            return this;
        }

        public Builder subscriptionsCount(Long subscriptionsCount) {
            this.subscriptionsCount = subscriptionsCount;
            return this;
        }

        public Builder reportsCount(Long reportsCount) {
            this.reportsCount = reportsCount;
            return this;
        }

        public AdminUserResponse build() {
            return new AdminUserResponse(this);
        }
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public String getBio() {
        return bio;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Boolean getIsBanned() {
        return isBanned;
    }

    public LocalDateTime getBannedUntil() {
        return bannedUntil;
    }

    public String getBanReason() {
        return banReason;
    }

    public Long getPostsCount() {
        return postsCount;
    }

    public Long getSubscribersCount() {
        return subscribersCount;
    }

    public Long getSubscriptionsCount() {
        return subscriptionsCount;
    }

    public Long getReportsCount() {
        return reportsCount;
    }
}
