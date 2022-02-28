/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.publication.utils;

import com.mass.sica.publication.entities.Auteur;
import com.mass.sica.publication.entities.Publication;
import de.undercouch.citeproc.csl.CSLName;
import de.undercouch.citeproc.csl.CSLType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Aristide MASSAGA
 */
public class PUBTypeToCSLType {

    public static CSLType convert(String type) {

        switch (type) {
            case "ARTICLE":
                return CSLType.ARTICLE;
            case "ARTICLE_JOURNAL":
                return CSLType.ARTICLE_JOURNAL;
            case "ARTICLE_MAGAZINE":
                return CSLType.ARTICLE_MAGAZINE;
            case "ARTICLE_NEWSPAPER":
                return CSLType.ARTICLE_NEWSPAPER;
            case "BILL":
                return CSLType.BILL;
            case "BOOK":
                return CSLType.BOOK;
            case "BROADCAST":
                return CSLType.BROADCAST;
            case "CHAPTER":
                return CSLType.CHAPTER;
            case "DATASET":
                return CSLType.DATASET;
            case "ENTRY":
                return CSLType.ENTRY;
            case "ENTRY_DICTIONARY":
                return CSLType.ENTRY_DICTIONARY;
            case "ENTRY_ENCYCLOPEDIA":
                return CSLType.ENTRY_ENCYCLOPEDIA;
            case "FIGURE":
                return CSLType.FIGURE;
            case "GRAPHIC":
                return CSLType.GRAPHIC;
            case "INTERVIEW":
                return CSLType.INTERVIEW;
            case "LEGAL_CASE":
                return CSLType.LEGAL_CASE;
            case "LEGISLATION":
                return CSLType.LEGISLATION;
            case "MANUSCRIPT":
                return CSLType.MANUSCRIPT;
            case "MAP":
                return CSLType.MAP;
            case "MOTION_PICTURE":
                return CSLType.MOTION_PICTURE;
            case "MUSICAL_SCORE":
                return CSLType.MUSICAL_SCORE;
            case "PAMPHLET":
                return CSLType.PAMPHLET;
            case "PAPER_CONFERENCE":
                return CSLType.PAPER_CONFERENCE;
            case "PATENT":
                return CSLType.PATENT;
            case "PERSONAL_COMMUNICATION":
                return CSLType.PERSONAL_COMMUNICATION;
            case "POST":
                return CSLType.POST;
            case "POST_WEBLOG":
                return CSLType.POST_WEBLOG;
            case "REPORT":
                return CSLType.REPORT;
            case "REVIEW":
                return CSLType.REVIEW;
            case "REVIEW_BOOK":
                return CSLType.REVIEW_BOOK;
            case "SONG":
                return CSLType.SONG;
            case "SPEECH":
                return CSLType.SPEECH;
            case "THESIS":
                return CSLType.THESIS;
            case "TREATY":
                return CSLType.TREATY;
            case "WEBPAGE":
                return CSLType.WEBPAGE;
            default:
                return CSLType.THESIS;
        }

    }

    public static CSLName[] convert(List<Auteur> coauteurs) {

        List<CSLName> names = new ArrayList<>();

        coauteurs.forEach(item -> names.add(PUBTypeToCSLType.convert(item)));

        return names.toArray(new CSLName[names.size()]);
    }

    public static CSLName convert(Auteur auteur) {

        Map<String, Object> author = new HashMap<>();

        author.put("family", auteur.getNom());
        author.put("given", auteur.getPrenom());

        return CSLName.fromJson(author);
    }

    public static String citation(Publication pub) {
        String result = "";

        result = pub.getAuteurs().stream().map(it -> it.getPrenom().charAt(0) + ". " + it.getNom()).collect(Collectors.joining(", ")) + ". "
                + pub.getTitre() + ". "
                + (pub.getColloque() == null ? pub.getUniversite() : pub.getColloque()) + ", "
                + pub.getAnneeSoutenance().getAnnee() + ". "
                + ("fr".equals(pub.getLangue()) ? "Français": "English") + ". "
                + (pub.getPageInterval() == null ? "" : pub.getPageInterval() + ". ")
                + "(" + pub.getCode() + ")";

        return result;
    }

}
