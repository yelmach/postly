package postly.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import postly.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
    @Query("SELECT u FROM UserEntity u where u.username = ?1 OR u.email = ?1")
    public Optional<UserEntity> findByUsernameOrEmail(String login);
    public boolean existsByUsername(String username);
    public boolean existsByEmail(String email);
}