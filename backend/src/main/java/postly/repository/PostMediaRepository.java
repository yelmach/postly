package postly.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import postly.entity.PostMediaEntity;

@Repository
public interface PostMediaRepository extends JpaRepository<PostMediaEntity, Long> {

    List<PostMediaEntity> findByPostIdOrderByCreatedAt(Long postId);

    void deleteByPostId(Long postId);

    List<PostMediaEntity> findByIsTemporaryTrueAndExpiresAtBefore(LocalDateTime dateTime);

    List<PostMediaEntity> findByMediaUrlIn(List<String> urls);
}
