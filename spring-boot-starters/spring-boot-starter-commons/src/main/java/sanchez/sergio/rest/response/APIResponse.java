package sanchez.sergio.rest.response;

import org.springframework.http.HttpStatus;

/**
 *
 * @author sergio
 */
public class APIResponse {
    
    private IResponseCodeTypes code;
    private ResponseStatus status = ResponseStatus.SUCCESS;
    private String message;
    private HttpStatus httpStatusCode = HttpStatus.OK;
    private String infoUrl;

    public APIResponse(IResponseCodeTypes code, ResponseStatus status, String message, HttpStatus httpStatusCode, String infoUrl) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
        this.infoUrl = infoUrl;
    }

    public APIResponse(IResponseCodeTypes code, String message) {
        this.code = code;
        this.message = message;
    }

    public IResponseCodeTypes getCode() {
        return code;
    }

    public void setCode(IResponseCodeTypes code) {
        this.code = code;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }

    public HttpStatus getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(HttpStatus httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    
}
