package sanchez.sergio.persistence.repositories;

import sanchez.sergio.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author sergio
 */
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT COUNT(u.id) FROM User u WHERE u.username=:username")
    Long existsUserWithUsername(@Param("username") String username);
    User findByUsername(String username);
}
