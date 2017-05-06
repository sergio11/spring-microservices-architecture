package sanchez.sergio.config.properties;

import java.io.Serializable;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author sergio
 */
@Component
@ConfigurationProperties(prefix="fcm")
public class FCMCustomProperties implements Serializable {
    
    private String appServerKey;
    private String notificationGroupsUrl;
    private String notificationSendUrl;

    public String getAppServerKey() {
        return appServerKey;
    }

    public void setAppServerKey(String appServerKey) {
        this.appServerKey = appServerKey;
    }

    public String getNotificationGroupsUrl() {
        return notificationGroupsUrl;
    }

    public void setNotificationGroupsUrl(String notificationGroupsUrl) {
        this.notificationGroupsUrl = notificationGroupsUrl;
    }

    public String getNotificationSendUrl() {
        return notificationSendUrl;
    }

    public void setNotificationSendUrl(String notificationSendUrl) {
        this.notificationSendUrl = notificationSendUrl;
    }
}
