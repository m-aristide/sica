/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mass.sica.domaine.entities;

import com.mass.sica.configs.AuditModel;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Aristide MASSAGA
 */
@Entity
@Table(name = "specialite")
@Data
@NoArgsConstructor
public class Specialite  extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String designation;
    
    private String url;
    
    @Column(columnDefinition = "text")
    private String description;
    private Integer stats;
    
    @NotNull
    @ManyToOne
    private Domaine domaine;
    
    @ManyToMany
    private List<TypePublication> typePublications;
    
    public void update(Specialite data) {
        this.description = data.description;
        this.designation = data.designation;
    }
    
}
