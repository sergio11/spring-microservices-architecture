package sanchez.sergio.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * @author sergio
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestTemplateConfig extends WebMvcConfigurerAdapter {
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Bean
    public MappingJackson2HttpMessageConverter converter(){
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        converter.setObjectMapper(objectMapper);
        return converter;
    }
    
     @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(converter());
    }

    @Bean
    public RestTemplate restTemplate(ObjectMapper objectMapper, List<ClientHttpRequestInterceptor> interceptors) {
        RestTemplate restTemplate = new RestTemplate(Collections.singletonList(converter()));
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
   
}
