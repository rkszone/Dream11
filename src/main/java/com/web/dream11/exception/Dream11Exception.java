package com.web.dream11.exception;

import org.springframework.http.HttpStatus;

public class Dream11Exception extends RuntimeException {

    private static final long serialVersionUID = 1L;
    String message;
    HttpStatus httpStatus;

    /**
     * Constructor dream11Exception
     * @param message the message of the error
     */
    public Dream11Exception(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus= httpStatus;
    }
}