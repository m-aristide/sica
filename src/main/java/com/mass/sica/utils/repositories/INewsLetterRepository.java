/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.utils.repositories;

import com.mass.sica.utils.entities.NewsLetter;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author sci2m
 */
@Repository
public interface INewsLetterRepository extends JpaRepository<NewsLetter, Long>, JpaSpecificationExecutor<NewsLetter> {

    
    public NewsLetter findByEmail(String email);
    
    public NewsLetter findByCode(String code);
    
    public List<NewsLetter> findByPublishedTrue();
}
