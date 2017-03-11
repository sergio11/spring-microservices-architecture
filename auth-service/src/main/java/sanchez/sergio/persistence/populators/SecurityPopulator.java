package sanchez.sergio.persistence.populators;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import sanchez.sergio.domain.Authority;
import sanchez.sergio.domain.AuthorityEnum;
import sanchez.sergio.domain.User;
import sanchez.sergio.persistence.repositories.AuthorityRepository;

/**
 *
 * @author sergio
 */
@Component
public class SecurityPopulator implements ApplicationListener<ContextRefreshedEvent> {
    
    private static Logger logger = LoggerFactory.getLogger(SecurityPopulator.class);
    
    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        logger.info("Create init security information ...");
        
        User user1 = new User();
        user1.setUsername("sergio11");
        user1.setPasswordClear("bisite00");
        user1.setConfirmPassword("bisite00");
        user1.setPassword("$2a$10$0eCQpFRdw8i6jJzjj/IuNuKpJYnLaO5Yp9xSJ3itcfPmQNXVhmNyu");

        Set<Authority> authorities = new HashSet();

        Authority authorityUser = new Authority();
        authorityUser.setType(AuthorityEnum.ROLE_USER);
        authorityUser.setDescription("Authority for normal users");
        authorityUser.addUser(user1);
        authorities.add(authorityUser);

        Authority authorityAdmin = new Authority();
        authorityAdmin.setType(AuthorityEnum.ROLE_ADMIN);
        authorityAdmin.setDescription("Authority for admin users");
        authorityAdmin.addUser(user1);
        authorities.add(authorityAdmin);

        try {
            authorityRepository.deleteAll();
            authorityRepository.save(authorities);
            logger.info("security information created ...");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }
    
}
