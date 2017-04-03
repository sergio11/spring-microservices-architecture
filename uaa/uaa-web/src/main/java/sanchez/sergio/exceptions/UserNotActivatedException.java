package sanchez.sergio.exceptions;

/**
 *
 * @author sergio
 */
public class UserNotActivatedException extends RuntimeException {

    public UserNotActivatedException() {
    }

    public UserNotActivatedException(String message) {
        super(message);
    }
}
