package sanchez.sergio.services.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sanchez.sergio.services.IPushNotificationsService;


/**
 * @author sergio
 */
@Service
public class PushNotificationsServiceImpl implements IPushNotificationsService {
    
    @Autowired
    private RestTemplate restTemplate;
   
    @Async
    @Override
    public CompletableFuture<String> createNotificationGroup(String notificationGroupName, List<String> deviceTokens) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Async
    @Override
    public CompletableFuture<String> createNotificationGroup(String notificationGroupName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Async
    @Override
    public CompletableFuture<String> addDevicesToGroup(String notificationGroupName, String notificationGroupKey, List<String> deviceTokens) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Async
    @Override
    public CompletableFuture<String> removeDevicesFromGroup(String notificationGroupName, String notificationGroupKey, List<String> deviceTokens) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
