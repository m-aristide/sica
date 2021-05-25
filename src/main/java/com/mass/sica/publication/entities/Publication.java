/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.publication.entities;

import com.mass.sica.configs.AuditModel;
import com.mass.sica.domaine.entities.Domaine;
import com.mass.sica.domaine.entities.Specialite;
import com.mass.sica.domaine.entities.TypePublication;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Aristide MASSAGA
 */
@Entity
@Data
@NoArgsConstructor
public class Publication extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String optio;
    @NotBlank
    private String titre;
    private String soustitre;
    @NotBlank
    private String motscle;

    @NotBlank
    @Column(columnDefinition = "text")
    private String resume;

    @Column(columnDefinition = "text")
    private String citation;

    @NotBlank
    @Column(columnDefinition = "text")
    private String astract;
    
    @NotBlank
    private String langue;
    
    private String url;
    
    @Column(length = 500)
    private String universite;
    
    @Column(length = 500)
    private String colloque;

    @Column(length = 3)
    private Integer version;

    private Integer nbVue;

    private Integer nbDownload;

    @NotNull
    @Column(length = 4)
    private Integer page;
    
    private Boolean accepter;

    @ManyToOne(fetch = javax.persistence.FetchType.EAGER)
    private AnneeSoutenance anneeSoutenance;

    @NotNull
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateSoutenance;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateAcceptation;

    @NotBlank
    private String fichier;
    
    private String image;

    @ManyToMany(fetch = javax.persistence.FetchType.EAGER)
    private List<Auteur> auteurs;

    @NotNull
    @ManyToOne(fetch = javax.persistence.FetchType.EAGER)
    private TypePublication type;

    @NotNull
    @ManyToOne(fetch = javax.persistence.FetchType.EAGER)
    private Specialite specialite;

    @NotNull
    @ManyToOne(fetch = javax.persistence.FetchType.EAGER)
    private Domaine domaine;

    public void update(Publication data) {
        this.optio = data.optio;
        this.titre = data.titre;
        this.soustitre = data.soustitre;
        this.motscle = data.motscle;
        this.resume = data.resume;
        this.astract = data.astract;
        this.langue = data.langue;
        this.page = data.page;
        this.dateSoutenance = data.dateSoutenance;
        this.universite = data.universite;
        this.colloque = data.colloque;
    }

}
