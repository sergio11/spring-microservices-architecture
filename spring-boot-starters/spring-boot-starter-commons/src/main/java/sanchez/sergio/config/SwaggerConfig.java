package sanchez.sergio.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import sanchez.sergio.config.properties.SwaggerCustomProperties;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author sergio
 */
@Configuration
@EnableSwagger2
@Profile("default")
@EnableConfigurationProperties(SwaggerCustomProperties.class)
public class SwaggerConfig {
    
    @Autowired
    private SwaggerCustomProperties swaggerCustomProperties;

    /**
     * Create Swagger Api configuration
     * @return Swagger Docket
     */
    @Bean
    public Docket sadrApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(swaggerCustomProperties.getGroupName())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .pathMapping(swaggerCustomProperties.getPathMapping())
                .useDefaultResponseMessages(false);
    }

    /**
     * Generate Api Info
     * @return Swagger API Info
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerCustomProperties.getApiInfo().getTitle())
                .description(swaggerCustomProperties.getApiInfo().getDescription())
                .version(swaggerCustomProperties.getApiInfo().getVersion())
                .license(swaggerCustomProperties.getApiInfo().getLicense())
                .licenseUrl(swaggerCustomProperties.getApiInfo().getLicenseUrl())
                .build();
    }
}
