package postly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import postly.entity.PostMediaEntity;

@Repository
public interface PostMediaRepository extends JpaRepository<PostMediaEntity, Long> {

    List<PostMediaEntity> findByPostIdOrderByDisplayOrder(Long postId);

    void deleteByPostId(Long postId);
}
