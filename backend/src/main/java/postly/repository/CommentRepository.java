package postly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import postly.entity.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

}
