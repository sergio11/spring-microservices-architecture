package sanchez.sergio.exceptions;

import sanchez.sergio.persistence.entities.User;

/**
 *
 * @author sergio
 */
public class UsernameAlredyExistsException extends RuntimeException {
    
    private User user;

    public UsernameAlredyExistsException(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
