package postly.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import postly.entity.LikeEntity;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    Optional<LikeEntity> findByUserIdAndPostId(Long userId, Long postId);

    long countByPostId(Long postId);

    boolean existsByUserIdAndPostId(Long userId, Long postId);

    void deleteByUserIdAndPostId(Long userId, Long postId);
}
