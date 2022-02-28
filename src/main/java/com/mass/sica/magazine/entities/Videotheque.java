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
public class Videotheque extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String titre;
    
    private String titrePara;
    
    private String titreComp;

    @Column(columnDefinition = "text")
    private String presentation;

    private String lien;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateRealisation;

    public void update(Videotheque data) {
        this.titre = data.titre;
        this.titrePara = data.titrePara;
        this.titreComp = data.titreComp;
        this.presentation = data.presentation;
        this.lien = data.lien;
        this.dateRealisation = data.dateRealisation;
    }

   
}
