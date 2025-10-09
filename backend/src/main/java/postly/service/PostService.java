package postly.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import postly.dto.request.CreatePostRequest;
import postly.dto.request.UpdatePostRequest;
import postly.dto.response.PostResponse;
import postly.entity.MediaType;
import postly.entity.PostEntity;
import postly.entity.PostMediaEntity;
import postly.entity.UserEntity;
import postly.exception.ApiException;
import postly.repository.PostMediaRepository;
import postly.repository.PostRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostMediaRepository postMediaRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserService userService;

    @Transactional
    public PostResponse createPost(CreatePostRequest request) {
        UserEntity currentUser = userService.getCurrentUserEntity();

        PostEntity post = new PostEntity();
        post.setUser(currentUser);
        post.setTitle(request.title());

        List<String> mediaUrls = new ArrayList<>();
        List<PostMediaEntity> mediaEntities = new ArrayList<>();

        if (request.files() != null && !request.files().isEmpty()) {
            for (MultipartFile file : request.files()) {
                String mediaUrl = fileStorageService.storePostMedia(file, post.getId());
                mediaUrls.add(mediaUrl);

                PostMediaEntity media = new PostMediaEntity();
                media.setPost(post);
                media.setMediaUrl(mediaUrl);
                media.setMediaType(determineMediaType(file));
                mediaEntities.add(media);
            }

            postMediaRepository.saveAll(mediaEntities);
        }

        String finalContent = replacePlaceholders(request.content(), mediaUrls);
        post.setContent(finalContent);
        post = postRepository.save(post);

        return PostResponse.fromPost(post, mediaEntities);
    }

    @Transactional
    public PostResponse updatePost(Long postId, UpdatePostRequest request) {
        PostEntity post = postRepository.findById(postId).orElseThrow(() -> ApiException.notFound("Post not found"));

        verifyOwnership(post);

        List<PostMediaEntity> existingMedia = postMediaRepository.findByPostIdOrderByCreatedAt(postId);

        Set<String> urlsInNewContent = extractMediaUrls(request.content());

        for (PostMediaEntity media : existingMedia) {
            if (!urlsInNewContent.contains(media.getMediaUrl())) {
                fileStorageService.deleteFile(media.getMediaUrl());
                postMediaRepository.delete(media);
            }
        }

        List<String> newMediaUrls = new ArrayList<>();
        if (request.files() != null && !request.files().isEmpty()) {
            for (MultipartFile file : request.files()) {
                String mediaUrl = fileStorageService.storePostMedia(file, postId);
                newMediaUrls.add(mediaUrl);

                PostMediaEntity media = new PostMediaEntity();
                media.setPost(post);
                media.setMediaUrl(mediaUrl);
                media.setMediaType(determineMediaType(file));
                postMediaRepository.save(media);
            }
        }

        String finalContent = replacePlaceholders(request.content(), newMediaUrls);

        post.setTitle(request.title());
        post.setContent(finalContent);
        post = postRepository.save(post);

        return mapToResponse(post);
    }

    @Transactional
    public void deletePost(Long postId) {
        PostEntity post = postRepository.findById(postId).orElseThrow(() -> ApiException.notFound("Post not found"));

        verifyOwnership(post);

        // Get all media and delete files
        List<PostMediaEntity> mediaFiles = postMediaRepository.findByPostIdOrderByCreatedAt(postId);
        for (PostMediaEntity media : mediaFiles) {
            fileStorageService.deleteFile(media.getMediaUrl());
        }

        // Delete post (cascade will delete media records)
        postRepository.delete(post);
    }

    public PostResponse getPost(Long postId) {
        PostEntity post = postRepository.findById(postId).orElseThrow(() -> ApiException.notFound("Post not found"));

        return mapToResponse(post);
    }

    public Page<PostResponse> getAllPosts(Pageable pageable) {
        Page<PostEntity> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        return posts.map(this::mapToResponse);
    }

    public Page<PostResponse> getUserPosts(Long userId, Pageable pageable) {
        Page<PostEntity> posts = postRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return posts.map(this::mapToResponse);
    }

    private void verifyOwnership(PostEntity post) {
        UserEntity currentUser = userService.getCurrentUserEntity();
        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw ApiException.forbidden("You can only modify your own posts");
        }
    }

    private String replacePlaceholders(String content, List<String> mediaUrls) {
        String result = content;

        for (int i = 0; i < mediaUrls.size(); i++) {
            String placeholder = "PLACEHOLDER_" + i;
            String actualUrl = mediaUrls.get(i);
            result = result.replace(placeholder, actualUrl);
        }

        return result;
    }

    private Set<String> extractMediaUrls(String content) {
        Set<String> urls = new HashSet<>();

        // Regex to match markdown image/video syntax: ![alt](url)
        Pattern pattern = Pattern.compile("!\\[.*?\\]\\((.*?)\\)");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String url = matcher.group(1);
            if (url.startsWith("/uploads/posts/")) {
                urls.add(url);
            }
        }

        return urls;
    }

    private MediaType determineMediaType(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType != null) {
            if (contentType.startsWith("image/")) {
                return MediaType.IMAGE;
            } else if (contentType.startsWith("video/")) {
                return MediaType.VIDEO;
            }
        }

        return MediaType.IMAGE;
    }

    private PostResponse mapToResponse(PostEntity post) {
        List<PostMediaEntity> mediaFiles = postMediaRepository.findByPostIdOrderByCreatedAt(post.getId());
        return PostResponse.fromPost(post, mediaFiles);
    }
}
