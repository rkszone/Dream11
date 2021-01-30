package com.web.dream11.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Iterator;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler extends Exception {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionMessage> handleAllExceptionMethod(Exception ex, WebRequest requset) {
        ExceptionMessage exceptionMessageObj = new ExceptionMessage();
        if(ex instanceof MethodArgumentNotValidException) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors();
            Iterator<FieldError> fieldError = fieldErrors.iterator();
            while(fieldError.hasNext()) {
                sb.append(fieldError.next().getDefaultMessage());
                if(fieldError.hasNext()){
                    sb.append(",");
                }
            }
            exceptionMessageObj.setMessage("Required fields are missing("+sb.toString()+")");
        }else{
            exceptionMessageObj.setMessage(ex.getLocalizedMessage());
        }
        LOGGER.error("Exception occurred in class "+ex.getClass().getCanonicalName()+" Detailed error log "+ex.getStackTrace());
        exceptionMessageObj.setError(ex.getClass().getCanonicalName());
        exceptionMessageObj.setPath(((ServletWebRequest) requset).getRequest().getServletPath());
        return new ResponseEntity<>(exceptionMessageObj, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);

    }
    @ExceptionHandler(Dream11Exception.class)
    public ResponseEntity<ExceptionMessage> handleApplicationExceptionMethod(Dream11Exception ex, WebRequest requset) {
        ExceptionMessage exceptionMessageObj = new ExceptionMessage();
        exceptionMessageObj.setMessage(ex.message);
        exceptionMessageObj.setError(ex.getClass().getCanonicalName());
        exceptionMessageObj.setPath(((ServletWebRequest) requset).getRequest().getServletPath());
        LOGGER.error("Exception occurred in class "+ex.getClass().getCanonicalName()+" Detailed error log "+ex.getStackTrace());
        return new ResponseEntity<>(exceptionMessageObj, new HttpHeaders(), ex.httpStatus);

    }
}