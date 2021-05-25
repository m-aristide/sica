/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.domaine.controlers;

import com.mass.sica.domaine.entities.Specialite;
import com.mass.sica.domaine.entities.TypePublication;
import com.mass.sica.domaine.projections.SpecialiteToPublish;
import com.mass.sica.domaine.repositories.IDomaineRepository;
import com.mass.sica.domaine.repositories.ISpecialiteRepository;
import com.mass.sica.domaine.repositories.ITypePublicationRepository;
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
public class SpecialiteControler {

    @Autowired
    private ISpecialiteRepository repository;

    @Autowired
    private IDomaineRepository domaineRepository;

    @Autowired
    private ITypePublicationRepository typePubRepository;

    @Secured("ROLE_ADMIN")
    @Transactional
    @PostMapping("/rest/specialite/")
    public ApiResponse<Boolean> nouvelleSpecialite(@Valid @RequestBody Specialite specialite) {
        specialite.setStats(0);
        specialite.setUrl(UtilsJob.buildUrl(specialite.getDesignation()));
        specialite.setDomaine(domaineRepository.getOne(specialite.getDomaine().getId()));
        this.repository.save(specialite);
        return new ApiResponse<>(true, true);
    }

    @GetMapping("/specialite/")
    public ApiResponse<List<Specialite>> specialites() {

        return new ApiResponse<>(true, this.repository.findByPublishedTrue());
    }

    @GetMapping("/specialite/{id}")
    public ApiResponse<Specialite> uneSpecialite(@PathVariable(value = "id") Long idSpecialite) {

        return new ApiResponse<>(true, this.repository.findById(idSpecialite).get());
    }

    @GetMapping("/specialite/topublish/")
    public ApiResponse<List<SpecialiteToPublish>> toPublish() {

        return new ApiResponse<>(true, this.repository.getByPublishedTrue());
    }

    @GetMapping("/specialite/bydomaine/{id}")
    public ApiResponse<List<SpecialiteToPublish>> byDomaine(@PathVariable(value = "id") Long idDomaine) {

        return new ApiResponse<>(true, this.repository.getByPublishedTrueAndDomaineId(idDomaine));
    }

    @Secured("ROLE_ADMIN")
    @Transactional
    @PutMapping("/rest/specialite/{id}")
    public ApiResponse<Boolean> modifierDomaine(@Valid @RequestBody Specialite data, @PathVariable(value = "id") Long idSpecialite) {
        try {
            Specialite specialite = this.repository.findById(idSpecialite).orElse(null);
            if (specialite == null) {
                throw new BadRequestException("Domaine invalide");
            }
            specialite.update(data);
            specialite.setUrl(UtilsJob.buildUrl(specialite.getDesignation()));
            specialite.setDomaine(domaineRepository.getOne(data.getDomaine().getId()));
            this.repository.save(specialite);
            return new ApiResponse<>(true, true);
        } catch (BadRequestException ex) {
            return new ApiResponse<>(false, ex.getMessage());
        }
    }

    @Secured("ROLE_ADMIN")
    @Transactional
    @DeleteMapping("/rest/specialite/{id}")
    public ApiResponse<Boolean> supprimerSpecialite(@PathVariable(value = "id") Long idSpecialite) {
        try {
            Specialite specialite = this.repository.findById(idSpecialite).get();
            specialite.setPublished(false);
            this.repository.save(specialite);
            return new ApiResponse<>(true, true);
        } catch (BadRequestException ex) {
            return new ApiResponse<>(false, ex.getMessage());
        }
    }

    @GetMapping("/specialite/typePublications/")
    public ApiResponse<List<TypePublication>> typePublications() {
        return new ApiResponse<>(true, this.typePubRepository.findAll());
    }
}
