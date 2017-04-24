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
    private Date createAt;

    public Notification(String payload) {
        this.payload = payload;
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

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
