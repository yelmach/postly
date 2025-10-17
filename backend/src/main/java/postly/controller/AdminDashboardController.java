package postly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import postly.dto.response.AdminPostResponse;
import postly.dto.response.AdminUserResponse;
import postly.dto.response.DashboardStatsResponse;
import postly.dto.response.ReportResponse;
import postly.entity.ReportStatus;
import postly.entity.Role;
import postly.service.AdminDashboardService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    @Autowired
    AdminDashboardService adminDashboardService;

    @GetMapping("/dashboard/stats")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        DashboardStatsResponse stats = adminDashboardService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<AdminUserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) Boolean banned,
            @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page, size);
        Page<AdminUserResponse> users = adminDashboardService.getAllUsers(pageable, role, banned, search);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/posts")
    public ResponseEntity<Page<AdminPostResponse>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Boolean hidden,
            @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page, size);
        Page<AdminPostResponse> posts = adminDashboardService.getAllPosts(pageable, hidden, search);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/reports")
    public ResponseEntity<Page<ReportResponse>> getAllReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) ReportStatus status,
            @RequestParam(required = false) String type) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ReportResponse> reports = adminDashboardService.getAllReports(pageable, status, type);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/reports/{id}")
    public ResponseEntity<ReportResponse> getReportById(@PathVariable Long id) {
        ReportResponse report = adminDashboardService.getReportById(id);
        return ResponseEntity.ok(report);
    }
}
