package postly.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import postly.dto.request.ReportRequest;
import postly.dto.request.ResolveReportRequest;
import postly.dto.response.ReportResponse;
import postly.entity.PostEntity;
import postly.entity.ReportEntity;
import postly.entity.ReportStatus;
import postly.entity.UserEntity;
import postly.exception.ApiException;
import postly.repository.PostRepository;
import postly.repository.ReportRepository;
import postly.repository.UserRepository;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public ReportResponse reportUser(Long userId, ReportRequest request) {
        UserEntity reporter = userService.getCurrentUserEntity();
        UserEntity reportedUser = userRepository.findById(userId)
                .orElseThrow(() -> ApiException.notFound("User not found"));

        // Prevent self-reporting
        if (reporter.getId().equals(reportedUser.getId())) {
            throw ApiException.badRequest("You cannot report yourself");
        }

        // Check for duplicate pending report
        Optional<ReportEntity> existingReport = reportRepository.findPendingUserReport(reporter.getId(), userId);
        if (existingReport.isPresent()) {
            throw ApiException.badRequest("You have already reported this user");
        }

        ReportEntity report = new ReportEntity();
        report.setReporter(reporter);
        report.setReportedUser(reportedUser);
        report.setReason(request.reason());
        report.setDescription(request.description());
        report.setStatus(ReportStatus.PENDING);

        report = reportRepository.save(report);
        return ReportResponse.fromReport(report);
    }

    @Transactional
    public ReportResponse reportPost(Long postId, ReportRequest request) {
        UserEntity reporter = userService.getCurrentUserEntity();
        PostEntity reportedPost = postRepository.findById(postId)
                .orElseThrow(() -> ApiException.notFound("Post not found"));

        // Prevent self-reporting
        if (reporter.getId().equals(reportedPost.getUser().getId())) {
            throw ApiException.badRequest("You cannot report your own post");
        }

        // Check for duplicate pending report
        Optional<ReportEntity> existingReport = reportRepository.findPendingPostReport(reporter.getId(), postId);
        if (existingReport.isPresent()) {
            throw ApiException.badRequest("You have already reported this post");
        }

        ReportEntity report = new ReportEntity();
        report.setReporter(reporter);
        report.setReportedPost(reportedPost);
        report.setReason(request.reason());
        report.setDescription(request.description());
        report.setStatus(ReportStatus.PENDING);

        report = reportRepository.save(report);
        return ReportResponse.fromReport(report);
    }

    public Page<ReportResponse> getAllReports(Pageable pageable, ReportStatus status, String type) {
        Page<ReportEntity> reports;

        if (status != null && type != null) {
            if ("user".equalsIgnoreCase(type)) {
                reports = reportRepository.findUserReportsByStatus(status, pageable);
            } else if ("post".equalsIgnoreCase(type)) {
                reports = reportRepository.findPostReportsByStatus(status, pageable);
            } else {
                reports = reportRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
            }
        } else if (status != null) {
            reports = reportRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
        } else if (type != null) {
            if ("user".equalsIgnoreCase(type)) {
                reports = reportRepository.findUserReports(pageable);
            } else if ("post".equalsIgnoreCase(type)) {
                reports = reportRepository.findPostReports(pageable);
            } else {
                reports = reportRepository.findAllByOrderByCreatedAtDesc(pageable);
            }
        } else {
            reports = reportRepository.findAllByOrderByCreatedAtDesc(pageable);
        }

        return reports.map(ReportResponse::fromReport);
    }

    public ReportResponse getReportById(Long reportId) {
        ReportEntity report = reportRepository.findById(reportId)
                .orElseThrow(() -> ApiException.notFound("Report not found"));

        return ReportResponse.fromReport(report);
    }

    @Transactional
    public ReportResponse resolveReport(Long reportId, ResolveReportRequest request) {
        UserEntity currentUser = userService.getCurrentUserEntity();

        ReportEntity report = reportRepository.findById(reportId)
                .orElseThrow(() -> ApiException.notFound("Report not found"));

        if (report.getStatus() != ReportStatus.PENDING) {
            throw ApiException.badRequest("Only pending reports can be resolved");
        }

        if (request.status() == ReportStatus.PENDING) {
            throw ApiException.badRequest("Cannot change status to PENDING");
        }

        report.setStatus(request.status());
        report.setAdminNotes(request.adminNotes());
        report.setReviewedBy(currentUser);
        report.setReviewedAt(LocalDateTime.now());

        report = reportRepository.save(report);
        return ReportResponse.fromReport(report);
    }
}
