/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.utils.entities;

import com.mass.sica.configs.AuditModel;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Aristide MASSAGA
 */
@Entity
@Data
@NoArgsConstructor
public class NewsLetter extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    private String email;

    private String code;

    public static String message(String type, String host, NewsLetter abonne) {

        switch (type) {
            case "welcome":
                return "<p>Bienvenue sur la plateforme d'archive ouverte Sciences In Cameroon (SICA). <br>"
                        + "<a href=\"" + host + "/news-letter/abonnement?code=" + abonne.getCode() + "\"> "
                        + "Cliquez ici pour confirmer votre souscription à la news-letter </a></p>"
                        + "<p> " + host + "/news-letter/abonnement?code=" + abonne.getCode() + " </p>"
                        + "<p>Vous serez notifier des dernières publications sur la plateforme</p>";

            default:
                return "";
        }

    }

}
