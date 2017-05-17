package sanchez.sergio.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import sanchez.sergio.rest.response.APIResponse;
import sanchez.sergio.rest.response.IResponseCodeTypes;

/**
 *
 * @author sergio
 */
public class ApiHelper {
    
    private static HttpHeaders getCommonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public static <T> ResponseEntity<APIResponse<T>> createAndSendResponse(APIResponse<T> response) {
        HttpHeaders headers = getCommonHeaders();
        ResponseEntity<APIResponse<T>> responseEntity = new ResponseEntity<APIResponse<T>>(response, headers, response.getHttpStatusCode());
        return responseEntity;
    }
    
    public static <T> ResponseEntity<APIResponse<T>> createAndSendResponse(IResponseCodeTypes responseCode, T data, HttpStatus httpStatusCode) { 
        HttpHeaders headers = getCommonHeaders();
        ResponseEntity<APIResponse<T>> responseEntity = new ResponseEntity<APIResponse<T>>(new APIResponse<T>(responseCode, data), headers, httpStatusCode);
        return responseEntity;
    }
    
}
