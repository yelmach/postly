package postly.dto.response;

import java.time.LocalDateTime;

import postly.entity.ReportEntity;
import postly.entity.ReportReason;
import postly.entity.ReportStatus;

public record ReportResponse(
        Long id,
        UserResponse reporter,
        UserResponse reportedUser,
        PostResponse reportedPost,
        ReportReason reason,
        String description,
        ReportStatus status,
        UserResponse reviewedBy,
        String adminNotes,
        LocalDateTime createdAt,
        LocalDateTime reviewedAt) {

    public static ReportResponse fromReport(ReportEntity report) {
        return new ReportResponse(
                report.getId(),
                report.getReporter() != null ? UserResponse.fromUser(report.getReporter()).build() : null,
                report.getReportedUser() != null ? UserResponse.fromUser(report.getReportedUser()).build() : null,
                report.getReportedPost() != null ? PostResponse.fromPost(report.getReportedPost()) : null,
                report.getReason(),
                report.getDescription(),
                report.getStatus(),
                report.getReviewedBy() != null ? UserResponse.fromUser(report.getReviewedBy()).build() : null,
                report.getAdminNotes(),
                report.getCreatedAt(),
                report.getReviewedAt());
    }
}
