/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mass.sica.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Aristide MASSAGA
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mail {
    
    private String destinataire;
    
    private String mailsender;
    
    private String sujet;
    
    private String body;

    public Mail(String destinataire, String sujet, String body) {
        this.destinataire = destinataire;
        this.sujet = sujet;
        this.body = body;
    }
    
}
