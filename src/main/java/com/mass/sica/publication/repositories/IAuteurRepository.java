/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.publication.repositories;

import com.mass.sica.publication.entities.Auteur;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author sci2m
 */
@Repository
public interface IAuteurRepository extends JpaRepository<Auteur, Long>, JpaSpecificationExecutor<Auteur> {
 
    public Auteur findByEmail(String email);
    
    public Auteur findByNom(String email);
    
    public List<Auteur> findByPublishedTrueAndNomLike(String lettre);
    
    public List<Auteur> findByPublishedTrue();
    
}
