/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mass.sica.utils;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Aristide MASSAGA
 */
@Data
@NoArgsConstructor
public class ApiResponse<T> implements Serializable {
    
    private Boolean success;
    
    private String message;
    
    private T content;

    public ApiResponse(Boolean success, String message, T content) {
        this.success = success;
        this.message = message;
        this.content = content;
    }

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
        this.content = null;
    }

    public ApiResponse(Boolean success, T content) {
        this.success = success;
        this.message = "SUCCESS";
        this.content = content;
    }
    
}
