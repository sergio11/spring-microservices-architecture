package sanchez.sergio.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

/**
 *
 * @author sergio
 */
public class APIResponse<T> {
    
    @JsonProperty("response_code_name")
    private IResponseCodeTypes code;
    @JsonProperty("response_status")
    private ResponseStatus status = ResponseStatus.SUCCESS;
    @JsonProperty("response_http_status")
    private HttpStatus httpStatusCode = HttpStatus.OK;
    @JsonProperty("response_info_url")
    private String infoUrl;
    @JsonProperty("response_data")
    private T data;

    public APIResponse(IResponseCodeTypes code, T data) {
        this.code = code;
        this.data = data;
    }
    
    public APIResponse(IResponseCodeTypes code, T data, HttpStatus httpStatusCode) {
        this.code = code;
        this.data = data;
        this.httpStatusCode = httpStatusCode;
    }

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
    
    @JsonProperty("response_code")
    public Long getCodeNumber() {
        return code.getResponseCode();
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
