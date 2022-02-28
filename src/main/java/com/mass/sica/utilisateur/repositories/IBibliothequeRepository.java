/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.utilisateur.repositories;

import com.mass.sica.utilisateur.entities.Bibliotheque;
import com.mass.sica.utilisateur.projections.BibliothequeProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author sci2m
 */
@Repository
public interface IBibliothequeRepository extends JpaRepository<Bibliotheque, Long>, JpaSpecificationExecutor<Bibliotheque> {
 
    public BibliothequeProjection findByUrl(String email);
    
    public List<BibliothequeProjection> findByCreatedByAndPublishedTrue(String username); 
    
    public Bibliotheque findByIdAndCreatedByAndPublishedTrue(Long id, String username); 
    
}
