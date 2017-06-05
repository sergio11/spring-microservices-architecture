package sanchez.sergio.util;

import sanchez.sergio.rest.response.IResponseCodeTypes;

/**
 *
 * @author sergio
 */
public enum UaaResponseCode implements IResponseCodeTypes {
    
    USERNAME_ALREDY_EXISTS(200L), EMAIL_ALREDY_EXISTS(201L);
   
    private Long code;
   
    private UaaResponseCode(Long code) {
        this.code = code;
    }
    
    @Override
    public Long getResponseCode() {
        return code;
    }
    
}
