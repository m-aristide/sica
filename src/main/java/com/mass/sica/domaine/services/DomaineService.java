/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.domaine.services;

import com.mass.sica.domaine.entities.TypePublication;
import com.mass.sica.domaine.repositories.IDomaineRepository;
import com.mass.sica.domaine.repositories.ISpecialiteRepository;
import com.mass.sica.domaine.repositories.ITypePublicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mass.sica.domaine.entities.Domaine;
import com.mass.sica.domaine.entities.Specialite;

/**
 *
 * @author Aristide MASSAGA
 */
@Service
public class DomaineService {

    @Autowired
    private IDomaineRepository domaineRepository;

    @Autowired
    private ISpecialiteRepository specialiteRepository;

    @Autowired
    private ITypePublicationRepository typePubRepository;
    
    public int setDomaineStat(Domaine domaine, int nb) {
        domaine.setStats(domaine.getStats() + nb);
        this.domaineRepository.save(domaine);

        return domaine.getStats();
    }
    
    public int setSpecialiteStat(Specialite specialite, int nb) {
        specialite.setStats(specialite.getStats() + nb);
        this.specialiteRepository.save(specialite);

        return specialite.getStats();
    }

    public int setTypePublicationStat(TypePublication type, int nb) {
        type.setStats(type.getStats() + nb);
        this.typePubRepository.save(type);

        return type.getStats();
    }

}
