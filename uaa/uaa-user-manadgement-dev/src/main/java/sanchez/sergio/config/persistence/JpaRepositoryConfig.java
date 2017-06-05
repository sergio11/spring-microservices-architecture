package sanchez.sergio.config.persistence;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author sergio
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"sanchez.sergio.persistence.repositories"})
@EnableTransactionManagement
public class JpaRepositoryConfig {
    
    private Logger logger = LoggerFactory.getLogger(JpaRepositoryConfig.class);
    
    @PostConstruct
    protected void init() {
        logger.info("init JPA Repositories ...");
    }
}
