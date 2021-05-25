/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.utils.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 *
 * @author Aristide MASSAGA
 */
@Service
@Slf4j
public class ModeEMAIL {

    @Value("${spring.mail.username}")
    private String mailsender;

    @Value("${app.host}")
    private String HOST;

    private JavaMailSender mailSender;

    private TemplateEngine templateEngine;

    @Autowired
    public ModeEMAIL(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void envoyer(String sujet, String message, String destinataire) throws MailException {

        /**
         * Envoie par interface
         */
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

            Context context = new Context();
            context.setVariable("message", message);
            context.setVariable("HOST", HOST);
            context.setVariable("destinataire", destinataire);

            messageHelper.setFrom(mailsender);
            messageHelper.setTo(destinataire);
            messageHelper.setSubject(sujet);

            messageHelper.setText(templateEngine.process("email-pdf/email", context), true);
        };

        mailSender.send(messagePreparator);
        log.info("Send mail : sujet = {}, message = {}, destinataire = {}", sujet, message, destinataire);
    }
}
