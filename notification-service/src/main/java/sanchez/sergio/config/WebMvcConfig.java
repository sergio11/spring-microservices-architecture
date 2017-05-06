package sanchez.sergio.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
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
}
