/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.magazine.services;

import com.mass.sica.magazine.entities.Magazine;
import com.mass.sica.magazine.repository.IMagazineRepository;
import com.mass.sica.utils.APIMessage;
import com.mass.sica.utils.exception.BadRequestException;
import com.mass.sica.utils.services.FileStorageService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Aristide MASSAGA
 */
@Slf4j
@Service
public class MagazineService {

    @Autowired
    private FileStorageService storageService;

    @Autowired
    private IMagazineRepository magazineRepository;

    public void nouvelleEdition(Magazine magazine) {

        magazine = magazineRepository.save(magazine);

        try {
            String fichier = storageService.moveFilePublication(magazine.getFichier(), "magazines", magazine.getId().toString());
            String image = storageService.moveFilePublication(magazine.getImage(), "magazines", magazine.getId().toString());
            magazine.setFichier(fichier);
            magazine.setImage(image);
        } catch (IOException ex1) {
        }
        magazineRepository.save(magazine);
    }

    public List<Magazine> magazines() {
        return magazineRepository.findByPublishedTrue();
    }

    public void modifierEdition(Magazine data, Long id) throws BadRequestException {
        Magazine mag = this.magazineRepository.findById(id).orElse(null);
        
        if (mag == null) {
            throw new BadRequestException(APIMessage.PUBLICATION_INVALIDE);
        }
        
        mag.update(data);
        
        try {
            // fichier
            URL url = new URL(data.getFichier());
            mag.setFichier(data.getFichier());  
        } catch (MalformedURLException ex) {
            try {
                String fichier = storageService.moveFilePublication(data.getFichier(), "magazines", data.getId().toString());
                mag.setFichier(fichier);
            } catch (IOException ex1) {
            }
        }
        
        try {
            // image
            URL url = new URL(data.getImage());
            mag.setFichier(data.getImage());
        } catch (MalformedURLException ex) {
            try {
                String image = storageService.moveFilePublication(data.getImage(), "magazines", data.getId().toString());
                mag.setImage(image);
            } catch (IOException ex1) {
            }
        }
        
        magazineRepository.save(mag);
    }
    
}
