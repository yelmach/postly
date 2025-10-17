package postly.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import postly.dto.request.BanUserRequest;
import postly.dto.request.ReportRequest;
import postly.dto.request.ResolveReportRequest;
import postly.dto.response.ReportResponse;
import postly.entity.ModerationAction;
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

    @Autowired
    private ModerationService moderationService;

    @Transactional
    public ReportResponse reportUser(Long userId, ReportRequest request) {
        UserEntity reporter = userService.getCurrentUserEntity();
        UserEntity reportedUser = userRepository.findById(userId)
                .orElseThrow(() -> ApiException.notFound("User not found"));

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

        if (request.action() != null && request.action() != ModerationAction.NO_ACTION) {
            executeModerationAction(report, request.action());
        }

        report.setStatus(request.status());
        report.setAdminNotes(request.adminNotes());
        report.setReviewedBy(currentUser);
        report.setReviewedAt(LocalDateTime.now());

        report = reportRepository.save(report);
        return ReportResponse.fromReport(report);
    }

    private void executeModerationAction(ReportEntity report, ModerationAction action) {
        switch (action) {
            case BAN_USER:
                if (report.getReportedUser() != null) {
                    BanUserRequest banRequest = new BanUserRequest(
                            report.getReason().toString(),
                            0,
                            true);
                    moderationService.banUser(report.getReportedUser().getId(), banRequest);
                } else {
                    throw ApiException.badRequest("Cannot ban user: This is a post report");
                }
                break;

            case DELETE_USER:
                if (report.getReportedUser() != null) {
                    moderationService.deleteUser(report.getReportedUser().getId());
                } else {
                    throw ApiException.badRequest("Cannot delete user: This is a post report");
                }
                break;

            case HIDE_POST:
                if (report.getReportedPost() != null) {
                    moderationService.hidePost(report.getReportedPost().getId(), report.getReason().toString());
                } else {
                    throw ApiException.badRequest("Cannot hide post: This is a user report");
                }
                break;

            case DELETE_POST:
                if (report.getReportedPost() != null) {
                    moderationService.deletePost(report.getReportedPost().getId());
                } else {
                    throw ApiException.badRequest("Cannot delete post: This is a user report");
                }
                break;

            default:
                break;
        }
    }
}
