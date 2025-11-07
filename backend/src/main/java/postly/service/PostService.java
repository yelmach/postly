package postly.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import postly.dto.request.PostRequest;
import postly.dto.response.PostResponse;
import postly.entity.PostEntity;
import postly.entity.PostMediaEntity;
import postly.entity.UserEntity;
import postly.exception.ApiException;
import postly.repository.PostMediaRepository;
import postly.repository.PostRepository;
import postly.repository.SubscriptionRepository;

@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostMediaRepository postMediaRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Transactional
    public PostResponse createPost(PostRequest request) {
        UserEntity currentUser = userService.getCurrentUserEntity();

        PostEntity post = new PostEntity();
        post.setUser(currentUser);
        post.setTitle(request.title());
        post.setContent(request.content());
        post = postRepository.save(post);

        if (request.mediaUrls() != null && !request.mediaUrls().isEmpty()) {
            List<PostMediaEntity> temporaryMedia = postMediaRepository.findByMediaUrlIn(request.mediaUrls());

            for (PostMediaEntity media : temporaryMedia) {
                if (media.getIsTemporary()) {
                    media.setPost(post);
                    media.setIsTemporary(false);
                    media.setExpiresAt(null);
                }
            }

            postMediaRepository.saveAll(temporaryMedia);
        }

        // Create notifications for all subscribers asynchronously
        List<Long> subscriberIds = subscriptionRepository.findBySubscribedToId(currentUser.getId())
                .stream()
                .map(subscription -> subscription.getSubscriber().getId())
                .collect(Collectors.toList());

        if (!subscriberIds.isEmpty()) {
            notificationService.createPostNotification(post.getId(), currentUser.getId(), subscriberIds);
        }

        return PostResponse.fromPost(post);
    }

    @Transactional
    public PostResponse updatePost(Long postId, PostRequest request) {
        PostEntity post = postRepository.findById(postId).orElseThrow(() -> ApiException.notFound("Post not found"));

        verifyOwnership(post);

        // Get current media linked to post
        List<PostMediaEntity> currentMedia = postMediaRepository.findByPostIdOrderByCreatedAt(postId);
        List<String> currentUrls = currentMedia.stream().map(PostMediaEntity::getMediaUrl).collect(Collectors.toList());

        // find and delete old media no longer in content
        List<String> urlsToRemove = currentUrls.stream()
                .filter(url -> !request.content().contains(url))
                .collect(Collectors.toList());

        if (!urlsToRemove.isEmpty()) {
            for (PostMediaEntity media : currentMedia) {
                if (urlsToRemove.contains(media.getMediaUrl())) {
                    postMediaRepository.delete(media);
                    fileStorageService.deleteFile(media.getMediaUrl());
                }
            }
        }

        if (request.mediaUrls() != null && !request.mediaUrls().isEmpty()) {
            List<PostMediaEntity> temporaryMedia = postMediaRepository.findByMediaUrlIn(request.mediaUrls());

            for (PostMediaEntity media : temporaryMedia) {
                if (media.getIsTemporary()) {
                    media.setPost(post);
                    media.setIsTemporary(false);
                    media.setExpiresAt(null);
                }
            }
            postMediaRepository.saveAll(temporaryMedia);
        }

        // Update post content and title
        post.setTitle(request.title());
        post.setContent(request.content());
        post = postRepository.save(post);

        return PostResponse.fromPost(post);
    }

    @Transactional
    public void deletePost(Long postId) {
        PostEntity post = postRepository.findById(postId).orElseThrow(() -> ApiException.notFound("Post not found"));

        verifyOwnership(post);

        List<PostMediaEntity> mediaFiles = new ArrayList<>(post.getMediaFiles());
        postRepository.delete(post);

        // Delete associated media files
        int failedDeletions = 0;
        for (PostMediaEntity media : mediaFiles) {
            boolean deleted = fileStorageService.deleteFile(media.getMediaUrl());
            if (!deleted) {
                failedDeletions++;
                logger.warn("Failed to delete media file for post {}: {}", postId, media.getMediaUrl());
            }
        }

        if (failedDeletions > 0) {
            logger.error("Post {} deleted but {} media file(s) could not be deleted", postId, failedDeletions);
        }
    }

    public PostResponse getPost(Long postId) {
        PostEntity post = postRepository.findById(postId).orElseThrow(() -> ApiException.notFound("Post not found"));
        UserEntity currentUser = userService.getCurrentUserEntity();

        if (post.getIsHidden() && !currentUser.isAdmin()) {
            throw ApiException.notFound("Post not found");
        }

        Long likesCount = likeService.countLikesByPost(postId);
        Long commentsCount = commentService.countCommentsByPost(postId);
        Boolean isLiked = likeService.isPostLikedByUser(postId, currentUser.getId());

        return PostResponse.fromPost(post, likesCount, commentsCount, isLiked);
    }

    public Page<PostResponse> getAllPosts(Pageable pageable) {
        UserEntity currentUser = userService.getCurrentUserEntity();

        Page<PostEntity> posts = postRepository.findPostsFromSubscribedUsers(currentUser.getId(), pageable);
        return posts.map(post -> {
            Long likesCount = likeService.countLikesByPost(post.getId());
            Long commentsCount = commentService.countCommentsByPost(post.getId());
            Boolean isLiked = likeService.isPostLikedByUser(post.getId(), currentUser.getId());
            return PostResponse.fromPost(post, likesCount, commentsCount, isLiked);
        });
    }

    public Page<PostResponse> getUserPosts(Long userId, Pageable pageable) {
        UserEntity currentUser = userService.getCurrentUserEntity();

        Page<PostEntity> posts = postRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return posts.map(post -> {
            Long likesCount = likeService.countLikesByPost(post.getId());
            Long commentsCount = commentService.countCommentsByPost(post.getId());
            Boolean isLiked = likeService.isPostLikedByUser(post.getId(), currentUser.getId());
            return PostResponse.fromPost(post, likesCount, commentsCount, isLiked);
        });
    }

    private void verifyOwnership(PostEntity post) {
        UserEntity currentUser = userService.getCurrentUserEntity();
        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw ApiException.forbidden("You can only modify your own posts");
        }
    }
}
