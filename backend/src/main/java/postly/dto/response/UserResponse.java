package postly.dto.response;

import postly.entity.Role;
import postly.entity.UserEntity;

public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String username;
    private String email;
    private Role role;
    private String bio;
    private String profileUrl;
    private Long postsCount;
    private Long subscribersCount;
    private Long subscribedCount;

    private UserResponse(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.fullName = builder.fullName;
        this.username = builder.username;
        this.email = builder.email;
        this.role = builder.role;
        this.bio = builder.bio;
        this.profileUrl = builder.profileUrl;
        this.postsCount = builder.postsCount;
        this.subscribersCount = builder.subscribersCount;
        this.subscribedCount = builder.subscribedCount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder fromUser(UserEntity user) {
        return new Builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .bio(user.getBio())
                .profileUrl(user.getProfileUrl());
    }

    public static class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private String fullName;
        private String username;
        private String email;
        private Role role;
        private String bio;
        private String profileUrl;
        private Long postsCount;
        private Long subscribersCount;
        private Long subscribedCount;

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

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder postsCount(Long postsCount) {
            this.postsCount = postsCount;
            return this;
        }

        public Builder followersCount(Long followersCount) {
            this.subscribersCount = followersCount;
            return this;
        }

        public Builder followingCount(Long followingCount) {
            this.subscribedCount = followingCount;
            return this;
        }

        public UserResponse build() {
            return new UserResponse(this);
        }
    }

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

    public String getFullName() {
        return fullName;
    }

    public Long getPostsCount() {
        return postsCount;
    }

    public Long getFollowersCount() {
        return subscribersCount;
    }

    public Long getFollowingCount() {
        return subscribedCount;
    }

    public boolean hasProfileImage() {
        return profileUrl != null && !profileUrl.trim().isEmpty();
    }

    public boolean hasBio() {
        return bio != null && !bio.trim().isEmpty();
    }
}