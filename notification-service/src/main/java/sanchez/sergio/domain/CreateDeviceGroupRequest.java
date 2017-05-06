package sanchez.sergio.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 *
 * @author sergio
 */
public class CreateDeviceGroupRequest implements Serializable {
    
    @JsonProperty("notification_group_name")
    private String notificationGroupName;
    @JsonProperty("user_id")
    private Long userId;

    public String getNotificationGroupName() {
        return notificationGroupName;
    }

    public void setNotificationGroupName(String notificationGroupName) {
        this.notificationGroupName = notificationGroupName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "CreateDeviceGroupRequest{" + "notificationGroupName=" + notificationGroupName + ", userId=" + userId + '}';
    }
}
