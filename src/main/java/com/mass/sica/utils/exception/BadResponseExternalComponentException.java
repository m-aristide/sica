/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.utils.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author sci2m
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadResponseExternalComponentException extends RuntimeException {
    
    public BadResponseExternalComponentException(String message) {
        super(message);
    }

    public BadResponseExternalComponentException(String message, Throwable cause) {
        super(message, cause);
    }
}
