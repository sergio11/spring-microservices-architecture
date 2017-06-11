package sanchez.sergio.config.persistence;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;

/**
 *
 * @author sergio
 */
@Configuration
@EnableLdapRepositories(basePackages = {"sanchez.sergio.persistence.repositories"})
public class LdapRepositoryConfig {
    
    private Logger logger = LoggerFactory.getLogger(LdapRepositoryConfig.class);
    
    @PostConstruct
    protected void init() {
        logger.info("init LDAP Repositories ...");
    }
}
