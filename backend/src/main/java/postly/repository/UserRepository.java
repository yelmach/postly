package postly.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import postly.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT u FROM UserEntity u where u.username = ?1 OR u.email = ?1")
    public Optional<UserEntity> findByUsernameOrEmail(String login);

    public boolean existsByUsername(String username);

    public boolean existsByEmail(String email);

    public Optional<UserEntity> findByUsername(String username);

    @Query("SELECT u FROM UserEntity u WHERE " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :query, '%'))")
    public List<UserEntity> searchUsers(@Param("query") String query);

    @Query("SELECT u FROM UserEntity u WHERE u.id != :currentUserId " +
           "AND u.id NOT IN (SELECT s.subscribedTo.id FROM SubscriptionEntity s WHERE s.subscriber.id = :currentUserId) " +
           "ORDER BY (SELECT COUNT(s2) FROM SubscriptionEntity s2 WHERE s2.subscribedTo.id = u.id) DESC")
    public List<UserEntity> findSuggestedUsers(@Param("currentUserId") Long currentUserId);
}