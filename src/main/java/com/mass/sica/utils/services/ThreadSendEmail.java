/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.utils.services;

import com.mass.sica.publication.entities.Publication;
import com.mass.sica.utils.entities.NewsLetter;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Aristide MASSAGA
 */
@Data
public class ThreadSendEmail extends Thread {

    private ModeEMAIL sendMail;

    private String HOST;

    private List<NewsLetter> abonnes;

    private List<Publication> pubs;

    @Override
    public void run() {

        String mesage = ThreadSendEmail.message(HOST, pubs);
        abonnes.forEach((abonne) -> {
            sendMail.envoyer("Nouvelles publications",  mesage, abonne.getEmail());
        });
    }

    public static String message(String host, List<Publication> pubs) {
        String result = "<p>Les nouvelles publications de la semaine disponibles sur le site SICA </p>";

        String listpub = "<ul>";

        for (Publication pub : pubs) {
            listpub += "<li style=\"padding: 10px !important;display: list-item !important;list-style: circle !important;\">"
                    + "<a href=\"" + host + "/consulter/publication/" + pub.getCode() + "\"> " + pub.getCitation() + "</a></li>";
        }
        listpub += "</ul>";

        return result + listpub;
    }

}
