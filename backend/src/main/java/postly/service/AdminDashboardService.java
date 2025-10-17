package postly.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import postly.dto.response.AdminPostResponse;
import postly.dto.response.AdminUserResponse;
import postly.dto.response.DashboardStatsResponse;
import postly.dto.response.DashboardStatsResponse.PostStats;
import postly.dto.response.DashboardStatsResponse.ReportStats;
import postly.dto.response.DashboardStatsResponse.UserStats;
import postly.dto.response.ReportResponse;
import postly.entity.PostEntity;
import postly.entity.ReportEntity;
import postly.entity.ReportStatus;
import postly.entity.Role;
import postly.entity.UserEntity;
import postly.exception.ApiException;
import postly.repository.CommentRepository;
import postly.repository.LikeRepository;
import postly.repository.PostRepository;
import postly.repository.ReportRepository;
import postly.repository.SubscriptionRepository;
import postly.repository.UserRepository;

@Service
public class AdminDashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    public DashboardStatsResponse getDashboardStats() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thirtyDaysAgo = now.minusDays(30);

        // User statistics
        long totalUsers = userRepository.count();
        long usersLast30Days = userRepository.countByCreatedAtAfter(thirtyDaysAgo);

        UserStats userStats = new UserStats(totalUsers, usersLast30Days);

        // Post statistics
        long totalPosts = postRepository.count();
        long postsLast30Days = postRepository.countByCreatedAtAfter(thirtyDaysAgo);

        PostStats postStats = new PostStats(totalPosts, postsLast30Days);

        // Report statistics
        long totalReports = reportRepository.count();
        long pendingReports = reportRepository.countByStatus(ReportStatus.PENDING);

        ReportStats reportStats = new ReportStats(totalReports, pendingReports);

        return new DashboardStatsResponse(userStats, postStats, reportStats);
    }

    public Page<AdminUserResponse> getAllUsers(Pageable pageable, Role role, Boolean banned, String search) {
        Page<UserEntity> users;

        // Handle search with filters
        if (search != null && !search.trim().isEmpty()) {
            if (role != null && banned != null) {
                users = userRepository.searchUsersByRoleAndBannedWithPagination(search, role, banned, pageable);
            } else if (role != null) {
                users = userRepository.searchUsersByRoleWithPagination(search, role, pageable);
            } else if (banned != null) {
                users = userRepository.searchUsersByBannedWithPagination(search, banned, pageable);
            } else {
                users = userRepository.searchUsersWithPagination(search, pageable);
            }
        } else {
            // No search, just filter
            if (role != null && banned != null) {
                users = userRepository.findByRoleAndIsBannedOrderByCreatedAtDesc(role, banned, pageable);
            } else if (role != null) {
                users = userRepository.findByRoleOrderByCreatedAtDesc(role, pageable);
            } else if (banned != null) {
                users = userRepository.findByIsBannedOrderByCreatedAtDesc(banned, pageable);
            } else {
                users = userRepository.findAllByOrderByCreatedAtDesc(pageable);
            }
        }

        return users.map(user -> {
            long postsCount = postRepository.countByUserId(user.getId());
            long subscribersCount = subscriptionRepository.countBySubscribedToId(user.getId());
            long subscriptionsCount = subscriptionRepository.countBySubscriberId(user.getId());
            long reportsCount = reportRepository.countReportsByUserId(user.getId());

            return AdminUserResponse.fromUser(user)
                    .postsCount(postsCount)
                    .subscribersCount(subscribersCount)
                    .subscriptionsCount(subscriptionsCount)
                    .reportsCount(reportsCount)
                    .build();
        });
    }

    public Page<AdminPostResponse> getAllPosts(Pageable pageable, Boolean hidden, String search) {
        Page<PostEntity> posts;

        if (search != null && !search.trim().isEmpty()) {
            if (hidden != null) {
                posts = postRepository.searchPostsByTitleAndStatus(search, hidden, pageable);
            } else {
                posts = postRepository.searchPostsByTitle(search, pageable);
            }
        } else {
            if (hidden != null) {
                posts = postRepository.findByIsHiddenOrderByCreatedAtDesc(hidden, pageable);
            } else {
                posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);
            }
        }

        return posts.map(post -> {
            long likesCount = likeRepository.countByPostId(post.getId());
            long commentsCount = commentRepository.countByPostId(post.getId());
            long reportsCount = reportRepository.countReportsByPostId(post.getId());

            return AdminPostResponse.fromPost(post, likesCount, commentsCount, reportsCount);
        });
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
}
