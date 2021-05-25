/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.notification.service;

import com.mass.sica.notification.entity.Mail;
import com.mass.sica.notification.exceptions.SendMailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

/**
 *
 * @author Aristide MASSAGA
 */
@Service
public class NotificationService implements INotificationService {

    @Value("${spring.mail.username}")
    private String mailsender;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Envoi de mail
     *
     * @param mail
     * @throws SendMailException
     */
    @Override
    public void mail(Mail mail) throws SendMailException {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

            messageHelper.setFrom(mailsender);
            messageHelper.setTo(mail.getDestinataire());
            messageHelper.setSubject(mail.getSujet());

            messageHelper.setText(mail.getBody(), true);
        };

        //try {
        mailSender.send(messagePreparator);
        //} catch (MailException ex) {
        //    throw new SendMailException(ex.getMessage());
        //}
    }

}
