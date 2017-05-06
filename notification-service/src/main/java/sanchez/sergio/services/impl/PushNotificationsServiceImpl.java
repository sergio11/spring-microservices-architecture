package sanchez.sergio.services.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sanchez.sergio.config.properties.FCMCustomProperties;
import sanchez.sergio.domain.DevicesGroupOperation;
import sanchez.sergio.services.IPushNotificationsService;


/**
 * @author sergio
 */
@Service
public class PushNotificationsServiceImpl implements IPushNotificationsService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private FCMCustomProperties firebaseCustomProperties;
   
    @Async
    @Override
    public CompletableFuture<String> createNotificationGroup(String notificationGroupName, List<String> deviceTokens) {
        return CompletableFuture.completedFuture(restTemplate.postForObject(firebaseCustomProperties.getNotificationGroupsUrl(), new DevicesGroupOperation(notificationGroupName, deviceTokens), String.class));
    }

    @Async
    @Override
    public CompletableFuture<String> createNotificationGroup(String notificationGroupName) {
        return CompletableFuture.completedFuture(restTemplate.postForObject(firebaseCustomProperties.getNotificationGroupsUrl(), new DevicesGroupOperation(notificationGroupName), String.class));
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
