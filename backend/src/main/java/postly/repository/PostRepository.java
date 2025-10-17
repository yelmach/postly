package postly.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import postly.entity.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    long countByUserId(Long userId);

    @Query("""
                SELECT p FROM PostEntity p
                WHERE (p.user.id IN (
                          SELECT s.subscribedTo.id
                          FROM SubscriptionEntity s
                          WHERE s.subscriber.id = :userId
                      )
                      OR p.user.id = :userId)
                  AND p.isHidden = false
                ORDER BY p.createdAt DESC
            """)
    Page<PostEntity> findPostsFromSubscribedUsers(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT p FROM PostEntity p WHERE p.user.id = :userId AND p.isHidden = false ORDER BY p.createdAt DESC")
    Page<PostEntity> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId, Pageable pageable);

    long countByCreatedAtAfter(LocalDateTime date);

    Page<PostEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<PostEntity> findByIsHiddenOrderByCreatedAtDesc(Boolean isHidden, Pageable pageable);

    @Query("SELECT p FROM PostEntity p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) ORDER BY p.createdAt DESC")
    Page<PostEntity> searchPostsByTitle(@Param("query") String query, Pageable pageable);

    @Query("SELECT p FROM PostEntity p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) AND p.isHidden = :isHidden ORDER BY p.createdAt DESC")
    Page<PostEntity> searchPostsByTitleAndStatus(@Param("query") String query, @Param("isHidden") Boolean isHidden,
            Pageable pageable);
}
