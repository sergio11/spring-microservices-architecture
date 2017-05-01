package sanchez.sergio.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import sanchez.sergio.helpers.HeaderRequestInterceptor;

/**
 * @author sergio
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    
    @Bean
    public List<ClientHttpRequestInterceptor> provideHttpRequestInterceptor(){
        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", "key="));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        return interceptors;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // move Swagger UI under /documentation
        registry.addRedirectViewController("/documentation/v2/api-docs", "/v2/api-docs?group=notifications");
        registry.addRedirectViewController("/documentation/swagger-resources/configuration/ui", "/swagger-resources/configuration/ui");
        registry.addRedirectViewController("/documentation/swagger-resources/configuration/security", "/swagger-resources/configuration/security");
        registry.addRedirectViewController("/documentation/swagger-resources", "/swagger-resources");
        registry.addRedirectViewController("/documentation/configuration/ui", "/configuration/ui");
        registry.addRedirectViewController("/documentation/configuration/security", "/configuration/security");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/documentation/swagger-ui.html**").addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
        registry.addResourceHandler("/documentation/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
