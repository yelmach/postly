package postly.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import postly.entity.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    long countByUserId(Long userId);

    // @Query("""
    //         SELECT p FROM Post p
    //             WHERE p.user.id IN (
    //                 SELECT s.subscribedTo.id
    //                 FROM Subscription s
    //                 WHERE s.subscriber.id = :userId
    //             )
    //             ORDER BY p.createdAt DESC
    //             """)
    // Page<PostEntity> findPostsFromSubscribedUsers(@Param("userId") Long userId, Pageable pageable);

    Page<PostEntity> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<PostEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
