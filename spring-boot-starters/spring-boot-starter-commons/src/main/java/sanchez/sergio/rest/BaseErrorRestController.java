/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanchez.sergio.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author sergio
 */
public abstract class BaseErrorRestController {
    
    
    protected ResponseEntity<RestApiError> createAndSendResponse(HttpStatus status, ApiErrorEnum apiError, String message){
        RestApiError restApiError = new RestApiError(status, apiError, message, message, this.getInfoUrl(apiError));
        return ApiHelper.createAndSendResponse(restApiError);
    }
    
    protected String getInfoUrl(ApiErrorEnum code){
        return "http://yourAppUrlToDocumentedApiCodes.com/api/support/" + code.ordinal();
    }
}
