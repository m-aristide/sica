/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mass.sica.magazine.controler;

import com.mass.sica.domaine.entities.Domaine;
import com.mass.sica.magazine.entities.Magazine;
import com.mass.sica.magazine.repository.IMagazineRepository;
import com.mass.sica.magazine.services.MagazineService;
import com.mass.sica.utils.ApiResponse;
import com.mass.sica.utils.UtilsJob;
import com.mass.sica.utils.exception.BadRequestException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Aristide MASSAGA
 */
@RestController
@RequestMapping("/api")
public class MagazineControler {

    @Autowired
    private MagazineService magazineService;
    
    
    @Autowired
    private IMagazineRepository magazineRepository;
    
    @GetMapping("/magazine/")
    public ApiResponse<List<Magazine>> magazines() {

        return new ApiResponse<>(true, this.magazineService.magazines());
    }
    
    @GetMapping("/magazine/{id}")
    public ApiResponse<Magazine> magazine(@PathVariable(value = "id") Long id) {

        return new ApiResponse<>(true, magazineRepository.findById(id).orElse(null));
    }
    
    @Secured("ROLE_ADMIN")
    @Transactional
    @PostMapping("/rest/magazine/")
    public ApiResponse<Boolean> nouvelleEdition(@Valid @RequestBody Magazine magazine) {
        magazineService.nouvelleEdition(magazine);
        return new ApiResponse<>(true, true);
    }
    
    
    @Secured("ROLE_ADMIN")
    @Transactional
    @PutMapping("/rest/magazine/{id}")
    public ApiResponse<Boolean> modifierEdition(@Valid @RequestBody Magazine magazine, @PathVariable(value = "id") Long id) {
        try {
            magazineService.modifierEdition(magazine, id);
            return new ApiResponse<>(true, true);
        } catch (BadRequestException ex) {
            return new ApiResponse<>(false, ex.getMessage());
        }
    }
    
    
    @Secured("ROLE_ADMIN")
    @Transactional
    @DeleteMapping("/rest/magazine/{id}")
    public ApiResponse<Boolean> supprimerEdition(@PathVariable(value = "id") Long id) {
        try {
            Magazine mag = this.magazineRepository.findById(id).get();
            mag.setPublished(false);
            this.magazineRepository.save(mag);
            return new ApiResponse<>(true, true);
        } catch (BadRequestException ex) {
            return new ApiResponse<>(false, ex.getMessage());
        }
    }

}
