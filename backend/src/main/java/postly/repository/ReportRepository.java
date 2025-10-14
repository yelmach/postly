package postly.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import postly.entity.ReportEntity;
import postly.entity.ReportStatus;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
	Page<ReportEntity> findByStatus(ReportStatus status, Pageable pageable);

	Page<ReportEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

	@Query("SELECT r FROM ReportEntity r WHERE r.status = :status ORDER BY r.createdAt DESC")
	Page<ReportEntity> findByStatusOrderByCreatedAtDesc(@Param("status") ReportStatus status, Pageable pageable);

	@Query("SELECT COUNT(r) FROM ReportEntity r WHERE r.reportedUser.id = :userId AND r.status = 'PENDING'")
	Long countPendingReportsByUserId(@Param("userId") Long userId);

	@Query("SELECT COUNT(r) FROM ReportEntity r WHERE r.reportedPost.id = :postId AND r.status = 'PENDING'")
	Long countPendingReportsByPostId(@Param("postId") Long postId);

	@Query("SELECT r FROM ReportEntity r WHERE r.reporter.id = :reporterId AND r.reportedUser.id = :reportedUserId AND r.status = 'PENDING'")
	Optional<ReportEntity> findPendingUserReport(@Param("reporterId") Long reporterId,
			@Param("reportedUserId") Long reportedUserId);

	@Query("SELECT r FROM ReportEntity r WHERE r.reporter.id = :reporterId AND r.reportedPost.id = :reportedPostId AND r.status = 'PENDING'")
	Optional<ReportEntity> findPendingPostReport(@Param("reporterId") Long reporterId,
			@Param("reportedPostId") Long reportedPostId);

	@Query("SELECT r FROM ReportEntity r WHERE r.reportedUser IS NOT NULL ORDER BY r.createdAt DESC")
	Page<ReportEntity> findUserReports(Pageable pageable);

	@Query("SELECT r FROM ReportEntity r WHERE r.reportedPost IS NOT NULL ORDER BY r.createdAt DESC")
	Page<ReportEntity> findPostReports(Pageable pageable);

	@Query("SELECT r FROM ReportEntity r WHERE r.reportedUser IS NOT NULL AND r.status = :status ORDER BY r.createdAt DESC")
	Page<ReportEntity> findUserReportsByStatus(@Param("status") ReportStatus status, Pageable pageable);

	@Query("SELECT r FROM ReportEntity r WHERE r.reportedPost IS NOT NULL AND r.status = :status ORDER BY r.createdAt DESC")
	Page<ReportEntity> findPostReportsByStatus(@Param("status") ReportStatus status, Pageable pageable);
}
