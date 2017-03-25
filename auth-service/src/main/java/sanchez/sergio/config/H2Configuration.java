package sanchez.sergio.config;

import org.h2.server.web.WebServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 *
 * @author sergio
 */
@Configuration
@Profile("default")
public class H2Configuration {
    
    private static Logger logger = LoggerFactory.getLogger(H2Configuration.class);
    
    @Bean
    protected ServletRegistrationBean h2servletRegistration(){
        logger.info("Register Servlet for H2 Console");
        ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet());
        registrationBean.addUrlMappings("/h2-console/*");
        return registrationBean;
    }
}
