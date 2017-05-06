package sanchez.sergio.persistence.repositories;

import sanchez.sergio.persistence.entities.Authority;
import sanchez.sergio.persistence.entities.AuthorityEnum;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author sergio
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByType(AuthorityEnum type);
}
