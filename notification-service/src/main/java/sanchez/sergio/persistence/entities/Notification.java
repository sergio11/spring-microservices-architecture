package sanchez.sergio.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author sergio
 */
@Entity
@Table(name = "NOTIFICATIONS")
public class Notification implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 90, unique = false)
    private String payload;
    
    @Column(name="username", nullable = false, unique = false)
    private String username;
    
    @Column(name="create_at", nullable = true)
    private Date createAt = new Date();
    
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "Notification{" + "id=" + id + ", payload=" + payload + ", createAt=" + createAt + '}';
    }
}
