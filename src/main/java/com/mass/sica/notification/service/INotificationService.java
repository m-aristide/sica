/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.notification.service;

import com.mass.sica.notification.entity.Mail;
import com.mass.sica.notification.exceptions.SendMailException;

/**
 *
 * @author Aristide MASSAGA
 */
public interface INotificationService {

    /**
     * Envoi de mail
     *
     * @param mail
     * @throws SendMailException
     */
    public void mail(Mail mail) throws SendMailException;

}
