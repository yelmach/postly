package postly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import postly.dto.request.ReportRequest;
import postly.dto.request.ResolveReportRequest;
import postly.dto.response.ReportResponse;
import postly.entity.ReportStatus;
import postly.service.ReportService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/users/{userId}")
    public ResponseEntity<ReportResponse> reportUser(@PathVariable Long userId,
            @Valid @RequestBody ReportRequest request) {
        ReportResponse report = reportService.reportUser(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }

    @PostMapping("/posts/{postId}")
    public ResponseEntity<ReportResponse> reportPost(@PathVariable Long postId,
            @Valid @RequestBody ReportRequest request) {
        ReportResponse report = reportService.reportPost(postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }

    @GetMapping
    public ResponseEntity<Page<ReportResponse>> getAllReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) ReportStatus status,
            @RequestParam(required = false) String type) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ReportResponse> reports = reportService.getAllReports(pageable, status, type);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponse> getReportById(@PathVariable Long id) {
        ReportResponse report = reportService.getReportById(id);
        return ResponseEntity.ok(report);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReportResponse> resolveReport(@PathVariable Long id,
            @Valid @RequestBody ResolveReportRequest request) {
        ReportResponse report = reportService.resolveReport(id, request);
        return ResponseEntity.ok(report);
    }
}
