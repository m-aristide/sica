/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mass.sica.utilisateur.entities;

import com.mass.sica.configs.AuditModel;
import com.mass.sica.publication.entities.Publication;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
public class Bibliotheque  extends AuditModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nom;
    
    @Column(columnDefinition = "text")
    private String description;

    @Column(length = 20)
    private String url;
    
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Publication> publications;

    public void update(Bibliotheque bib) {
        this.nom = bib.nom;
        this.description = bib.description;
    }
}
