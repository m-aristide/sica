/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.domaine.repositories;

import com.mass.sica.domaine.entities.TypePublication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author sci2m
 */
@Repository
public interface ITypePublicationRepository extends JpaRepository<TypePublication, Long>, JpaSpecificationExecutor<TypePublication> {
 
    
}
