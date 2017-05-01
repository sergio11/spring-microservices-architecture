package sanchez.sergio.domain;

import org.springframework.stereotype.Component;

/**
 * @author sergio
 */
@Component
public class UserHolder {

    private static final ThreadLocal<Long> user = new ThreadLocal<Long>();

    public static void setUser(Long userId) {
        user.set(userId);
    }

    public static Long getUser() {
        return user.get();
    }

    public static void clearUser() {
        user.remove();
    }
}
