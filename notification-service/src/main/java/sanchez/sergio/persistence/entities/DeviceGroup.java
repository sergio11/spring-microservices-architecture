package sanchez.sergio.persistence.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author sergio
 */
@Entity
@Table(name = "DEVICE_GROUPS")
public class DeviceGroup implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="notification_key_name", nullable = false, unique = true, length = 120)
    private String notificationKeyName;
    
    @Column(name="notification_key", nullable = false, unique = true, length = 120)
    private String notificationKey;
    
    @Column(name="user_id", nullable = false, unique = true)
    private Long userId;
    
    @Column(name="create_at", nullable = true)
    private Date createAt = new Date();
    
    @OneToMany(mappedBy = "deviceGroup", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Device> devices = new ArrayList();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotificationKeyName() {
        return notificationKeyName;
    }

    public void setNotificationKeyName(String notificationKeyName) {
        this.notificationKeyName = notificationKeyName;
    }

    public String getNotificationKey() {
        return notificationKey;
    }

    public void setNotificationKey(String notificationKey) {
        this.notificationKey = notificationKey;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    @Override
    public String toString() {
        return "DeviceGroup{" + "id=" + id + ", notificationKeyName=" + notificationKeyName + ", notificationKey=" + notificationKey + ", userId=" + userId + ", createAt=" + createAt + '}';
    }
}
