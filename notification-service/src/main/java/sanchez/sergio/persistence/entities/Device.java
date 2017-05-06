package sanchez.sergio.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author sergio
 */
@Entity
@Table(name = "DEVICES")
public class Device implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="registration_token", nullable = false, length = 120, unique = true)
    private String registrationToken;
    
    @Column(name="device_type", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private DeviceTypeEnum deviceTypeEnum;
    
    @ManyToOne(optional=false, fetch = FetchType.EAGER)
    private DeviceGroup deviceGroup;
    
    @Column(name="create_at", nullable = true)
    private Date createAt = new Date();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    public DeviceTypeEnum getDeviceTypeEnum() {
        return deviceTypeEnum;
    }

    public void setDeviceTypeEnum(DeviceTypeEnum deviceTypeEnum) {
        this.deviceTypeEnum = deviceTypeEnum;
    }

    public DeviceGroup getDeviceGroup() {
        return deviceGroup;
    }

    public void setDeviceGroup(DeviceGroup deviceGroup) {
        this.deviceGroup = deviceGroup;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "Device{" + "id=" + id + ", registrationToken=" + registrationToken + ", deviceTypeEnum=" + deviceTypeEnum + ", deviceGroup=" + deviceGroup + ", createAt=" + createAt + '}';
    }
    
    
}
