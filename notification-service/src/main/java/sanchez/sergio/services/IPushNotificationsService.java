package sanchez.sergio.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author sergio
 */
public interface IPushNotificationsService {
    CompletableFuture<String> createNotificationGroup(String notificationGroupName, List<String> deviceTokens);
    CompletableFuture<String> createNotificationGroup(String notificationGroupName);
    CompletableFuture<String> addDevicesToGroup(String notificationGroupName, String notificationGroupKey, List<String> deviceTokens);
    CompletableFuture<String> removeDevicesFromGroup(String notificationGroupName, String notificationGroupKey, List<String> deviceTokens);
}