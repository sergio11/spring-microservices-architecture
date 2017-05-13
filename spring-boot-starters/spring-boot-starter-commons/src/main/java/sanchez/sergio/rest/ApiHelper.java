package sanchez.sergio.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author sergio
 */
public class ApiHelper {

    public static ResponseEntity<RestApiError> createAndSendResponse(RestApiError restApiError) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<RestApiError> responseEntity = new ResponseEntity<RestApiError>(restApiError, headers, restApiError.getHttpStatusCode());
        return responseEntity;
    }
}
