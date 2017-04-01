package sanchez.sergio.config;

import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan(basePackages = {"sanchez.sergio.domain"})
@EnableJpaRepositories(basePackages = {"sanchez.sergio.persistence.repositories"})
@EnableTransactionManagement
@EnableConfigurationProperties(LiquibaseProperties.class)
public class DatabaseConfiguration {
    
    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);
   
    @Bean
    public SpringLiquibase liquibase(DataSource dataSource, LiquibaseProperties liquibaseProperties) {

        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:config/liquibase/master.xml");
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        liquibase.setShouldRun(liquibaseProperties.isEnabled());
        log.debug("Configuring Liquibase");
        return liquibase;
    }
}
