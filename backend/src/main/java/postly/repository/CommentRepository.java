package postly.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import postly.entity.CommentEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);

    long countByPostId(Long postId);
}
