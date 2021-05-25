/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.utils.services;

import com.mass.sica.publication.entities.Publication;
import com.mass.sica.publication.repositories.IPublicationRepository;
import com.mass.sica.utils.DaysCalculator;
import com.mass.sica.utils.UtilsJob;
import com.mass.sica.utils.entities.Contacter;
import com.mass.sica.utils.entities.NewsLetter;
import com.mass.sica.utils.repositories.IContacterRepository;
import com.mass.sica.utils.repositories.INewsLetterRepository;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Aristide MASSAGA
 */
@Service
public class UtilsService {

    @Value("${app.host}")
    private String HOST;

    @Autowired
    private INewsLetterRepository abonneRepository;

    @Autowired
    private IContacterRepository contacterRepository;

    @Autowired
    private ModeEMAIL sendMail;

    @Autowired
    private IPublicationRepository pubRepository;

    /**
     * diffusion d'une nouvelle publication à tous les abonnées de la news
     * letter
     *
     * @param date
     */
    public void sendNewsLetters(Date date) {
        ThreadSendEmail myThread = new ThreadSendEmail();
        myThread.setAbonnes(abonneRepository.findByPublishedTrue());
        myThread.setHOST(HOST);
        myThread.setSendMail(sendMail);

        List<Publication> pubs = pubRepository.findByDateAcceptationBetweenAndPublishedTrue(DaysCalculator.addDaysAtTimeZero(date, -7),
                DaysCalculator.dateAtTimeZero(date));

        myThread.setPubs(pubs);
        myThread.start();
    }

    public boolean abonnementNewsLetter(NewsLetter abonne) throws MailException {

        // test si adresse n'a pas déjà un abonnement en cours
        NewsLetter existe = abonneRepository.findByEmail(abonne.getEmail());

        if (existe != null && !existe.isPublished()) {
            // si l'email n'est pas activé, on renoie le code d'activation
            abonne.setCode(existe.getCode());
        } else {
            abonne.setCode(UtilsJob.codeGenerator());
            abonne.setPublished(false);
            abonne = abonneRepository.save(abonne);
        }

        sendMail.envoyer("Abonnement à la news letter SICA", NewsLetter.message("welcome", HOST, abonne), abonne.getEmail());

        return true;
    }

    /**
     * confirmation d'abonnement à la news-letter
     *
     * @param code
     * @return
     */
    public NewsLetter confirmationAbonnementNewsLetter(String code) {

        NewsLetter abonne = abonneRepository.findByCode(code);

        if (abonne != null) {
            abonne.setPublished(true);
            abonneRepository.save(abonne);
        }

        return abonne;
    }

    /**
     * désabonnement de la news-letter
     *
     * @param email
     * @return
     */
    public NewsLetter desabonnementNewsLetter(String email) {

        NewsLetter abonne = abonneRepository.findByEmail(email);

        if (abonne != null) {
            abonne.setPublished(false);
            abonneRepository.save(abonne);
        }

        return abonne;
    }

    /**
     * enregistrement du formulaire de contact
     *
     * @param contact
     * @return
     * @throws MailException
     */
    public Contacter contacter(Contacter contact) throws MailException {
        //  enregistrement du formulaire
        contact.setCreatedAt(new Date());
        contact = contacterRepository.save(contact);

        // envoie d'une copie du formulaire à la personne par mail
        sendMail.envoyer("Message envoyé au support service de SICA : " + contact.getObjet(), contact.getMessage(), contact.getEmail());

        return contact;
    }

    public Page<Contacter> nousContacter(Pageable pgble) {
        return contacterRepository.findAll(pgble);
    }

    /**
     * récupérer les abonnés à la news letter
     *
     * @param pgble
     * @return
     */
    public Page<NewsLetter> abonnesNewsLetter(Pageable pgble) {
        return abonneRepository.findAll(pgble);
    }
}
