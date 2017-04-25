package sanchez.sergio.domain;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author sergio
 */
public class Notification implements Serializable {
    
    private Long id;
    private String payload;
    private Long userId;
    private Date createAt;

    public Notification() {}
    
    public Notification(String payload, Long userId) {
        this.payload = payload;
        this.userId = userId;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
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
}
