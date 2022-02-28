/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.utils.controlers;

import com.mass.sica.utils.APIMessage;
import com.mass.sica.utils.ApiResponse;
import com.mass.sica.utils.dto.Actualite;
import com.mass.sica.utils.entities.Contacter;
import com.mass.sica.utils.entities.NewsLetter;
import com.mass.sica.utils.services.UtilsService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Aristide MASSAGA
 */
@RestController
@RequestMapping("/api")
public class UtilsControler {

    @Autowired
    private UtilsService utilsService;

    @Transactional
    @PostMapping("/news-letter/enregistrement")
    public ApiResponse<Boolean> abonnementNewsLetter(@Valid @RequestBody NewsLetter abonne) {

        try {
            utilsService.abonnementNewsLetter(abonne);
            return new ApiResponse<>(true, APIMessage.ABONNEMENT_NEWSLETTER_ENREGISTRE, true);
        } catch (MailException ex) {
            return new ApiResponse<>(false, APIMessage.ABONNEMENT_NEWSLETTER_ECHEC, false);
        }
    }

    @GetMapping("/news-letter/abonnement")
    public ApiResponse<Boolean> confirmationAbonnement(@RequestParam("code") String code) {
        utilsService.confirmationAbonnementNewsLetter(code);
        return new ApiResponse<>(true, APIMessage.ABONNEMENT_NEWSLETTER_REUSSI, true);
    }

    @GetMapping("/news-letter/unsubscribe")
    public ApiResponse<Boolean> desabonnement(@RequestParam("user") String email) {

        utilsService.desabonnementNewsLetter(email);

        return new ApiResponse<>(true, APIMessage.ABONNEMENT_NEWSLETTER_UNREGISTER, true);
    }

    @GetMapping("/news-letter")
    public ApiResponse<Page<NewsLetter>> abonnesNewsLetter(Pageable pgble) {
        return new ApiResponse<>(true, utilsService.abonnesNewsLetter(pgble));
    }

    @Transactional
    @PostMapping("/nous-contacter")
    public ApiResponse<Boolean> nousContacter(@Valid @RequestBody Contacter contact) {
        try {
            utilsService.contacter(contact);
            return new ApiResponse<>(true, APIMessage.MESSAGE_RECU, true);
        } catch (MailException ex) {
            return new ApiResponse<>(false, APIMessage.EMAIL_INVALIDE, false);
        }
    }

    @GetMapping("/nous-contacter")
    public ApiResponse<Page<Contacter>> nousContacter(Pageable pgble) {
        return new ApiResponse<>(true, utilsService.nousContacter(pgble));
    }

    @GetMapping("/actualites")
    public ApiResponse<List<Actualite>> actualites() {
        return new ApiResponse<>(true, utilsService.actualites());
    }

}
