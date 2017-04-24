package sanchez.sergio.delegates;

import org.springframework.stereotype.Component;
import sanchez.sergio.domain.Notification;

/**
 * @author sergio
 */
@Component("deadLettersDelegate")
public class DeadLettersDelegate implements INotificationDelegate {

    @Override
    public void handleMessage(Notification notification) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
