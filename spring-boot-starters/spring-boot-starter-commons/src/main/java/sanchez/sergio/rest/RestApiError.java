/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanchez.sergio.rest;

import org.springframework.http.HttpStatus;

/**
 * Rest api error.
 * @author sergio
 */
public class RestApiError {
    
    private final HttpStatus httpStatusCode;
    private final ApiErrorEnum apiError;
    private final String message;
    private final String devMessage;
    private final String infoUrl;

    public RestApiError(HttpStatus httpStatusCode, ApiErrorEnum apiCode, String message, String devMessage, String infoUrl) {
        this.httpStatusCode = httpStatusCode;
        this.apiError = apiCode;
        this.message = message;
        this.devMessage = devMessage;
        this.infoUrl = infoUrl;
    }

    public HttpStatus getHttpStatusCode() {
        return this.httpStatusCode;
    }
 
    public ApiErrorEnum getApiError() {
        return this.apiError;
    }

    public String getMessage() {
        return this.message;
    }

    public String getDeveloperMessage() {
        return this.devMessage;
    }

    public String getInfoUrl() {
        return this.infoUrl;
    }
}
