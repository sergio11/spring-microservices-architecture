package sanchez.sergio.persistence.repositories;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import sanchez.sergio.persistence.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sanchez.sergio.persistence.entities.AccountStatus;

/**
 *
 * @author sergio
 */
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT COUNT(u.id) FROM User u WHERE u.username=:username")
    Long existsUserWithUsername(@Param("username") String username);
    Optional<User> findByUsername(String username);
    Optional<User> findAccountByEmail(@Param("email") String email);
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUsername(String username);
    List<User> findAllByStatusAndCreatedAtBefore(AccountStatus status, ZonedDateTime dateTime);
}
