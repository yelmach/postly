package postly.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import postly.service.LikeService;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping
    public ResponseEntity<Map<String, Boolean>> toggleLike(@PathVariable Long postId) {
        boolean isLiked = likeService.toggleLike(postId);
        return ResponseEntity.ok(Map.of("liked", isLiked));
    }
}
