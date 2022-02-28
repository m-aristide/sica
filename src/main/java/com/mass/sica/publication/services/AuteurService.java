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

        if ("null@null.null".equals(auteur.getEmail())) {
            auteur.setEmail(null);
        }

        if (auteur.getId() != null) {
            Auteur autor = this.auteurRepository.findById(auteur.getId()).orElse(null);
            if (autor != null) {
                autor.update(auteur);
                return this.auteurRepository.save(autor);
            }
        }

        if (auteur.getEmail() != null) {
            Auteur autor = this.auteurRepository.findByEmail(auteur.getEmail());
            if (autor != null) {
                autor.update(auteur);
                return this.auteurRepository.save(autor);
            }
        }

        return this.auteurRepository.save(auteur);

        /*if (auteur.getId() == null) {
            return this.auteurRepository.save(auteur);
        }
        Auteur autor = this.auteurRepository.findById(auteur.getId()).orElse(null);
        if (autor == null) {
            // vérification email
            autor = this.auteurRepository.findByEmail(auteur.getEmail());
            if (autor == null) {
                auteur.setId(null);
                return this.auteurRepository.save(auteur);
            } else {
                autor.update(auteur);
                return this.auteurRepository.save(autor);
            }
        }
        autor.update(auteur);
        return this.auteurRepository.save(autor);*/
    }

}
