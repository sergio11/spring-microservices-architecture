package sanchez.sergio.persistence.repositories;

import sanchez.sergio.domain.Authority;
import sanchez.sergio.domain.AuthorityEnum;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author sergio
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByType(AuthorityEnum type);
}
