package postly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import postly.entity.LikeEntity;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    
}
