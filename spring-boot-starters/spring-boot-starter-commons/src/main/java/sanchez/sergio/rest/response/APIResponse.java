package sanchez.sergio.rest.response;

import org.springframework.http.HttpStatus;

/**
 *
 * @author sergio
 */
public class APIResponse<T> {
    
    private IResponseCodeTypes code;
    private ResponseStatus status = ResponseStatus.SUCCESS;
    private HttpStatus httpStatusCode = HttpStatus.OK;
    private String infoUrl;
    private T data;

    public APIResponse(IResponseCodeTypes code, T data, String infoUrl) {
        this.code = code;
        this.data = data;
        this.infoUrl = infoUrl;
    }

    public APIResponse(IResponseCodeTypes code, ResponseStatus status, HttpStatus httpStatusCode, String infoUrl, T data) {
        this.code = code;
        this.status = status;
        this.httpStatusCode = httpStatusCode;
        this.infoUrl = infoUrl;
        this.data = data;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    
}
