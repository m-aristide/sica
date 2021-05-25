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
public interface PublicationProjection extends Serializable {
    
    public Long getId();

    public String getUrl();
    
    public String getTitre();
    
    public String getSoustitre();
    
    public String getUniversite();
    
    public String getColloque();
    
    public String getCode();
    
    public Date getDateSoutenance();
    
    public String getLangue();
    
    public String getFichier();
    
}
