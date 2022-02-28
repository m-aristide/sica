/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.utilisateur.repositories;

import com.mass.sica.utilisateur.entities.Utilisateur;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author sci2m
 */
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long>, JpaSpecificationExecutor<Utilisateur> {
    
    public Optional<Utilisateur> findByUsername(String username);

    public Optional<Utilisateur> findByEmail(String val);
    
    public Optional<Utilisateur> findByActive(String val);
    
    public Page<Utilisateur> findByActiveAndRole(String val, String role, Pageable pgbl);
    
    public Page<Utilisateur> findByActive(String val, Pageable pgbl);
    
}
