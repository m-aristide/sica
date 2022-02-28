/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mass.sica.utils.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Aristide MASSAGA
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Actualite implements Serializable, Comparable<Actualite> {

    private Long id;
    
    private String content;
    
    private String url;
    
    private Date date;
    
    private Boolean isMagazine;

    @Override
    public int compareTo(Actualite arg0) {
        Actualite e = (Actualite) arg0;
        return e.getDate().compareTo(getDate());
    }
}
