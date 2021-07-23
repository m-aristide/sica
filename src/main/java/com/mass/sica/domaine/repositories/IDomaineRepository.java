/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.domaine.repositories;

import com.mass.sica.domaine.entities.Domaine;
import com.mass.sica.domaine.projections.DomaineToPublish;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author sci2m
 */
@Repository
public interface IDomaineRepository extends JpaRepository<Domaine, Long>, JpaSpecificationExecutor<Domaine> {

    public List<Domaine> findByPublishedTrue();

    @Query(nativeQuery = true, value = "SELECT dm.id, dm.designation, dm.stats FROM domaine dm WHERE dm.published = true")
    public List<DomaineToPublish> getPublishedTrue();

}
