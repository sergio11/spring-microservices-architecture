package sanchez.sergio.delegates;

import sanchez.sergio.domain.Notification;

/**
 *
 * @author sergio
 */
public interface INotificationDelegate {
    void handleMessage(Notification notification);
}
