package sanchez.sergio.config.properties;

import java.io.Serializable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author sergio
 */
@Component
@ConfigurationProperties(prefix="swagger")
public class SwaggerCustomProperties implements Serializable {
    
    private String groupName;
    private String pathMapping;
    private final ApiInfo apiInfo = new ApiInfo();

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPathMapping() {
        return pathMapping;
    }

    public void setPathMapping(String pathMapping) {
        this.pathMapping = pathMapping;
    }

    public ApiInfo getApiInfo() {
        return apiInfo;
    }
    
    public static class ApiInfo {
        
        private String title;
        private String description;
        private String version;
        private String license;
        private String licenseUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getLicense() {
            return license;
        }

        public void setLicense(String license) {
            this.license = license;
        }

        public String getLicenseUrl() {
            return licenseUrl;
        }

        public void setLicenseUrl(String licenseUrl) {
            this.licenseUrl = licenseUrl;
        }
    }
    
}
