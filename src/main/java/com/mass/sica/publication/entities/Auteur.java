/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.publication.entities;

import com.mass.sica.configs.AuditModel;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Aristide MASSAGA
 */
@Entity
@Data
@NoArgsConstructor
public class Auteur extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    @NotBlank
    private String email;

    private String telephone;

    private String universite;

    private Long userId;

    public void update(Auteur auteur) {
        this.nom = auteur.nom;
        this.prenom = auteur.prenom;
        this.email = auteur.email;
        this.telephone = auteur.telephone;
        this.universite = auteur.universite;
    }

}
