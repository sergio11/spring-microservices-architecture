package sanchez.sergio.messages.admin;

import java.io.Serializable;

/**
 * @author sergio
 */
public class UsersManadgementMessage implements Serializable {
    
    private Long id;
    private UsersManadgementMessageType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsersManadgementMessageType getType() {
        return type;
    }

    public void setType(UsersManadgementMessageType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Message{" + "id=" + id + ", type=" + type + '}';
    }
   
}
