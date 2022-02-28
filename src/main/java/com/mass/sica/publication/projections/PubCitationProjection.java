/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.publication.projections;

import java.io.Serializable;
import java.util.Date;


/**
 *
 * @author Aristide MASSAGA
 */
public interface PubCitationProjection extends Serializable {
    
    public Long getId();
    
    public String getCitation();    
}
