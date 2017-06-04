package sanchez.sergio.exceptions;

import sanchez.sergio.persistence.entities.User;

/**
 *
 * @author sergio
 */
public class EmailAlredyExistsException extends RuntimeException {
    
    private User user;

    public EmailAlredyExistsException(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
