package sanchez.sergio.persistence.repositories;

import org.springframework.data.ldap.repository.LdapRepository;
import sanchez.sergio.persistence.entities.User;

/**
 * @author sergio
 */
public interface UserRepository extends LdapRepository<User> {
    
}
