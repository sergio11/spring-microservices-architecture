package sanchez.sergio.persistence.repositories;

import org.springframework.data.ldap.repository.LdapRepository;
import sanchez.sergio.persistence.entities.Authority;

/**
 *
 * @author sergio
 */
public interface AuthorityRepository extends LdapRepository<Authority> {
    
}
