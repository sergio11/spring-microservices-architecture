package sanchez.sergio.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sergio
 */
public class DevicesGroupOperation implements Serializable {
    
    @JsonProperty("operation")
    private DevicesGroupOperationType operation;
    @JsonProperty("notification_key_name")
    private String groupName;
    @JsonProperty("notification_key")
    private String groupKey;
    @JsonProperty("registration_ids")
    private List<String> deviceTokens = new ArrayList<String>();
    
    public DevicesGroupOperation(String groupName, List<String> deviceTokens) {
        this.operation = DevicesGroupOperationType.CREATE;
        this.groupName = groupName;
        this.deviceTokens = deviceTokens;
    }

    public DevicesGroupOperation(DevicesGroupOperationType operation, String groupName, String groupKey, List<String> deviceTokens) {
        this.operation = operation;
        this.groupName = groupName;
        this.groupKey = groupKey;
        this.deviceTokens = deviceTokens;
    }

    public DevicesGroupOperationType getOperation() {
        return operation;
    }

    public void setOperation(DevicesGroupOperationType operation) {
        this.operation = operation;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public List<String> getDeviceTokens() {
        return deviceTokens;
    }

    public void setDeviceTokens(List<String> deviceTokens) {
        this.deviceTokens = deviceTokens;
    }
}
