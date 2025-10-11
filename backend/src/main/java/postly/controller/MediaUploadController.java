package postly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import postly.dto.response.PostMediaResponse;
import postly.service.MediaUploadService;

@RestController
@RequestMapping("/api/media")
public class MediaUploadController {

    @Autowired
    private MediaUploadService mediaUploadService;

    @PostMapping("/upload")
    public ResponseEntity<PostMediaResponse> uploadMedia(@RequestParam("file") MultipartFile file) {
        PostMediaResponse response = mediaUploadService.saveMedia(file);
        return ResponseEntity.ok(response);
    }
}
