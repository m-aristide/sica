/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.publication.entities;

import com.mass.sica.configs.AuditModel;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Aristide MASSAGA
 */
@Entity
@Table(name = "annee_soutenance")
@Data
@NoArgsConstructor
public class AnneeSoutenance extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4)
    private int annee;

    @Column(length = 2)
    private int mois;

    @Column(length = 6)
    private int stats;

    public AnneeSoutenance(Date date) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        this.annee = now.get(Calendar.YEAR);
        this.mois = now.get(Calendar.MONTH) + 1;
    }
    
    public static Map<String, Integer> statitstiques(List<AnneeSoutenance> annees) {
        Map<String, Integer> map = new HashMap<>();
        annees.forEach(annee -> {
            // chaque année a son nombre de publication
            map.put( String.valueOf(annee.getAnnee()) , annee.getStats());
        });
        return map;
    }

}
