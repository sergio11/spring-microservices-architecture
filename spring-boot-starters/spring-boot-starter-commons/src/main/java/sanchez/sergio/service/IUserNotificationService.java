package sanchez.sergio.service;

/**
 *
 * @author sergio
 */
public interface IUserNotificationService {
    boolean notifyUserConnected(String username);
    boolean notifyUserDisconnected(String username);
}
