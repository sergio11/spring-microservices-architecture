package sanchez.sergio.notifications.messages.admin;

import java.io.Serializable;

/**
 * @author sergio
 */
public class UsersManadgementMessage implements Serializable {
    
    private String username;
    private UsersManadgementMessageType type;

    public UsersManadgementMessage(String username, UsersManadgementMessageType type) {
        this.username = username;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public UsersManadgementMessageType getType() {
        return type;
    }

    public void setType(UsersManadgementMessageType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Message{" + "id=" + username + ", type=" + type + '}';
    }
   
}
