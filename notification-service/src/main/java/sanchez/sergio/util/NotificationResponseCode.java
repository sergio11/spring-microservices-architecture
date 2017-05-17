package sanchez.sergio.util;

import sanchez.sergio.rest.response.IResponseCodeTypes;

/**
 *
 * @author sergio
 */
public enum NotificationResponseCode implements IResponseCodeTypes {
    
    ALL_NOTIFICATIONS(100L), SELF_NOTIFICATIONS(101L), ALL_DEVICES_INTO_GROUP(102L),
    DEVICES_GROUP_CREATED(103L), DEVICES_GROUP_CREATE_FAILED(104L), DEVICE_ADDED_TO_GROUP(105L),
    DEVICE_ADD_TO_GROUP_FAILED(106L), DEVICE_REMOVED_FROM_GROUP(107L), DEVICE_GROUP_NOT_FOUND(108L);
    
    private Long code;
    
    private NotificationResponseCode(Long code){
        this.code = code;
    }
   
    @Override
    public Long getResponseCode() {
        return this.code;
    }
}
