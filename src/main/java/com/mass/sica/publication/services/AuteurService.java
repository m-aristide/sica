/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.publication.services;

import com.mass.sica.publication.entities.Auteur;
import com.mass.sica.publication.repositories.IAuteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Aristide MASSAGA
 */
@Service
public class AuteurService {

    @Autowired
    private IAuteurRepository auteurRepository;

    public Auteur createUpdateAuteur(Auteur auteur) {
        if (auteur.getId() == null) {
            return this.auteurRepository.save(auteur);
        }
        Auteur autor = this.auteurRepository.findById(auteur.getId()).orElse(null);
        if (autor == null) {
            auteur.setId(null);
            return this.auteurRepository.save(auteur);
        }
        autor.update(auteur);
        return this.auteurRepository.save(autor);
    }

}
