package sanchez.sergio.rest.response;

/**
 *
 * @author sergio
 */
public enum CommonResponseCode implements IResponseCodeTypes {
    
    ACCESS_DENIED(500L), SECURITY_ERROR(501L), GENERIC_ERROR(502L), RESOURCE_NOT_FOUND(503L);
    
    private Long code;

    private CommonResponseCode(Long code) {
        this.code = code;
    }

    @Override
    public Long getResponseCode() {
       return code;
    }
    
}
