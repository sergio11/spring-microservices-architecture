package sanchez.sergio.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import sanchez.sergio.rest.response.APIResponse;

/**
 *
 * @author sergio
 */
public class ApiHelper {

    public static ResponseEntity<APIResponse> createAndSendResponse(APIResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<APIResponse> responseEntity = new ResponseEntity<APIResponse>(response, headers, response.getHttpStatusCode());
        return responseEntity;
    }
}
