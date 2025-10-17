package postly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import postly.dto.request.ReportRequest;
import postly.service.ReportService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/users/{userId}")
    public ResponseEntity<Void> reportUser(@PathVariable Long userId,
            @Valid @RequestBody ReportRequest request) {
        reportService.reportUser(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/posts/{postId}")
    public ResponseEntity<Void> reportPost(@PathVariable Long postId,
            @Valid @RequestBody ReportRequest request) {
        reportService.reportPost(postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
