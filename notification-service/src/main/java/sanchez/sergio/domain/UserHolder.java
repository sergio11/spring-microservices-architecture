package sanchez.sergio.domain;

import org.springframework.stereotype.Component;

/**
 * @author sergio
 */
@Component
public class UserHolder {

    private static final ThreadLocal<String> user = new ThreadLocal<String>();

    public static void setUser(String username) {
        user.set(username);
    }

    public static String getUser() {
        return user.get();
    }

    public static void clearUser() {
        user.remove();
    }
}
