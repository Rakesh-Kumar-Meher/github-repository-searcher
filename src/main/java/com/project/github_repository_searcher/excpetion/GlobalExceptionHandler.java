package com.project.github_repository_searcher.excpetion;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;

@RestControllerAdvice 
public class GlobalExceptionHandler {
    
    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<Map<String, String>> handleGithubApiException(RestClientResponseException ex){
        HttpStatus status;
        String message;

        if(ex.getStatusCode().value() == 403 || ex.getStatusCode().value() == 429){
            status = HttpStatus.SERVICE_UNAVAILABLE;
            message = "Github API rate limit may have been exceeded. " + "Please try again later";
        }else if(ex.getStatusCode().is5xxServerError()){
            status = HttpStatus.BAD_GATEWAY;
            message = "Github service is temporarily unavailable";
        }else{
            status = HttpStatus.BAD_GATEWAY;
            message = "Github API request failed";
        }

        return ResponseEntity
            .status(status)
            .body(Map.of(
                "error", message, 
                "githubStatus", String.valueOf(ex.getStatusCode().value())
            ));
    }
}
