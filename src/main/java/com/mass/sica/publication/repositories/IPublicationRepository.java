/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.publication.repositories;

import com.mass.sica.publication.entities.Publication;
import com.mass.sica.publication.projections.PublicationProjection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author sci2m
 */
@Repository
public interface IPublicationRepository extends JpaRepository<Publication, Long>, JpaSpecificationExecutor<Publication> {
 
    public Page<Publication> findByPublishedTrueAndAccepterFalse(Pageable page);
    
    public Page<Publication> findByPublishedTrue(Pageable page);
    
    public Page<Publication> findByPublishedTrueAndAccepterTrue(Pageable page);
    
    public Page<PublicationProjection> findByPublishedAndAccepterTrue(boolean pub, Pageable page);
    
    public List<Publication> findByPublishedTrueAndAccepterTrueAndAuteursEmail(String email);
    
    public List<Publication> findByAuteursIdAndPublishedTrue(Long idAuteur);
    
    public List<Publication> findByDateAcceptationBetweenAndPublishedTrue(Date start, Date end);
    
    public Page<Publication> findByCreatedByAndPublishedTrue(String creator, Pageable page);
    
    public Page<PublicationProjection> findAllProjectedBy(Specification<Publication> s, Pageable pgbl);
    
    public Publication findByUrl(String url);
    
    public Optional<Publication> findByCode(String code);
    
    public int countBySpecialiteIdAndPublishedTrueAndAccepterTrue(Long idSpecialite);
    
}
