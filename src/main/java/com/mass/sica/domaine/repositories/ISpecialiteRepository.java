/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.domaine.repositories;

import com.mass.sica.domaine.entities.Specialite;
import com.mass.sica.domaine.projections.SpecialiteToPublish;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author sci2m
 */
@Repository
public interface ISpecialiteRepository extends JpaRepository<Specialite, Long>, JpaSpecificationExecutor<Specialite> {
 
    public List<Specialite> findByPublishedTrue();
 
    public List<SpecialiteToPublish> getByPublishedTrue();
    
    public List<SpecialiteToPublish> getByPublishedTrueAndDomaineId(Long id);
 
    public List<Specialite> findByDomaineIdAndPublishedTrue(Long idDomaine);
}
