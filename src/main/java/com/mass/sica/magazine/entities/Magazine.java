/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.magazine.entities;

import com.mass.sica.configs.AuditModel;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Aristide MASSAGA 
 */
@Entity
@Data
@NoArgsConstructor
public class Magazine extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String titre;
    
    private String titrePara;
    
    private String titreComp;

    private String directeurPub;

    private String lieuEdition;
    
    private String editeur;

    private String limiteChrono;
    
    private String collection;

    private String directeurColl;

    private String issn;

    private String fichier;

    private String image;

    @Column(length = 7)
    private int prix;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datePub;

    public void update(Magazine data) {
        this.titre = data.titre;
        this.titrePara = data.titrePara;
        this.titreComp = data.titreComp;
        this.directeurPub = data.directeurPub;
        this.lieuEdition = data.lieuEdition;
        this.editeur = data.editeur;
        this.limiteChrono = data.limiteChrono;
        this.collection = data.collection;
        this.directeurColl = data.directeurColl;
        this.issn = data.issn;
        this.prix = data.prix;
        this.datePub = data.datePub;
    }

   
    
    
    
}
