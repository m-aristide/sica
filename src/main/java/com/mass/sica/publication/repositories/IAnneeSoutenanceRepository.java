/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.publication.repositories;

import com.mass.sica.publication.entities.AnneeSoutenance;
import com.mass.sica.publication.projections.AnneeSoutenanceProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Aristide MASSAGA
 */
@Repository
public interface IAnneeSoutenanceRepository extends JpaRepository<AnneeSoutenance, Long>, JpaSpecificationExecutor<AnneeSoutenance> {
 
    public AnneeSoutenance findByMoisAndAnnee(int mois, int annee);
 
    public List<AnneeSoutenance> findByAnneeOrderByMoisAsc(int annee);
    
    @Query(nativeQuery = true, value = "SELECT SUM(stats) AS stats, annee FROM annee_soutenance GROUP BY annee ORDER BY annee DESC")
    public List<AnneeSoutenanceProjection> selectOrderByAnneeDesc();
}
