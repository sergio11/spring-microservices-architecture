/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanchez.sergio.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sanchez.sergio.rest.response.APIResponse;
import sanchez.sergio.rest.response.IResponseCodeTypes;
import sanchez.sergio.rest.response.ResponseStatus;

/**
 *
 * @author sergio
 */
public abstract class BaseErrorRestController {
    
    protected ResponseEntity<APIResponse<String>> createAndSendResponse(HttpStatus status, IResponseCodeTypes responseCode, String message){
        APIResponse restApiError = new APIResponse(responseCode, ResponseStatus.ERROR, status, this.getInfoUrl(responseCode), message);
        return ApiHelper.<String>createAndSendResponse(restApiError);
    }
    
    protected String getInfoUrl(IResponseCodeTypes responseCode){
        return "http://yourAppUrlToDocumentedApiCodes.com/api/support/" + responseCode.getResponseCode();
    }
}
