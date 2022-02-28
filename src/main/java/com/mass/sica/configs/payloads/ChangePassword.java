/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mass.sica.configs.payloads;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author Aristide MASSAGA
 */
@Data
public class ChangePassword implements Serializable {

    
    private String newpassword;
    
    private String password;
    
}
