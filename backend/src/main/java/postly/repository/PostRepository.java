package postly.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import postly.entity.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    long countByUserId(Long userId);

    List<PostEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

    Page<PostEntity> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<PostEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
