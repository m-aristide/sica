/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mass.sica.domaine.projections;

import com.mass.sica.domaine.entities.Domaine;

/**
 *
 * @author Aristide MASSAGA
 */
public interface SpecialiteProjection {

    public Long getId();

    public String getDesignation();

    public String getUrl();

    public String getDescription();

    public Integer getStats();

    public Domaine getDomaine();
    
}
