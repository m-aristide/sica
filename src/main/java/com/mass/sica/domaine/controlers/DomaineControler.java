/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.domaine.controlers;

import com.mass.sica.domaine.entities.Domaine;
import com.mass.sica.domaine.projections.DomaineToPublish;
import com.mass.sica.domaine.repositories.IDomaineRepository;
import com.mass.sica.publication.entities.Publication;
import com.mass.sica.publication.repositories.IPublicationRepository;
import com.mass.sica.publication.utils.PUBTypeToCSLType;
import com.mass.sica.utils.APIMessage;
import com.mass.sica.utils.ApiResponse;
import com.mass.sica.utils.UtilsJob;
import com.mass.sica.utils.exception.BadRequestException;
import com.mass.sica.utils.services.FileStorageService;
import de.undercouch.citeproc.CSL;
import de.undercouch.citeproc.csl.CSLItemData;
import de.undercouch.citeproc.csl.CSLItemDataBuilder;
import java.io.File;
import java.io.IOException;
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
public class DomaineControler {

    @Autowired
    private IDomaineRepository repository;

    @Autowired
    private IPublicationRepository pubRepository;

    @Autowired
    private FileStorageService storageService;

    @Secured("ROLE_ADMIN")
    @Transactional
    @PostMapping("/rest/domaine/")
    public ApiResponse<Boolean> nouvelleDomaine(@Valid @RequestBody Domaine domaine) {
        domaine.setStats(0);
        domaine.setUrl(UtilsJob.buildUrl(domaine.getDesignation()));
        this.repository.save(domaine);
        return new ApiResponse<>(true, true);
    }

    @Transactional
    @PostMapping("/create/domaines")
    public ApiResponse<Boolean> nouvellesDomaines() throws IOException, Exception {

        Publication pub = pubRepository.findById(1L).get();

        return new ApiResponse<>(true, true);
    }

    @GetMapping("/domaine/")
    public ApiResponse<List<Domaine>> domaines() {

        return new ApiResponse<>(true, this.repository.findByPublishedTrue());
    }

    @GetMapping("/domaine/topublish")
    public ApiResponse<List<DomaineToPublish>> domainesTopublish() {
        return new ApiResponse<>(true, this.repository.getPublishedTrue());
    }

    @GetMapping("/domaine/{id}")
    public ApiResponse<Domaine> uneDomaine(@PathVariable(value = "id") Long idDomaine) {

        return new ApiResponse<>(true, this.repository.findById(idDomaine).get());
    }

    @Secured("ROLE_ADMIN")
    @Transactional
    @PutMapping("/rest/domaine/{id}")
    public ApiResponse<Boolean> modifierDomaine(@Valid @RequestBody Domaine data, @PathVariable(value = "id") Long idDomaine) {
        try {
            Domaine domaine = this.repository.findById(idDomaine).orElse(null);
            if (domaine == null) {
                throw new BadRequestException(APIMessage.DOMAINE_INVALIDE);
            }
            domaine.update(data);
            domaine.setUrl(UtilsJob.buildUrl(domaine.getDesignation()));
            this.repository.save(domaine);
            return new ApiResponse<>(true, true);
        } catch (BadRequestException ex) {
            return new ApiResponse<>(false, ex.getMessage());
        }
    }

    @Secured("ROLE_ADMIN")
    @Transactional
    @DeleteMapping("/rest/domaine/{id}")
    public ApiResponse<Boolean> supprimerDomaine(@PathVariable(value = "id") Long idDomaine) {
        try {
            Domaine domaine = this.repository.findById(idDomaine).get();
            if (domaine == null) {
                throw new BadRequestException(APIMessage.DOMAINE_INVALIDE);
            }
            domaine.setPublished(false);
            this.repository.save(domaine);
            return new ApiResponse<>(true, true);
        } catch (BadRequestException ex) {
            return new ApiResponse<>(false, ex.getMessage());
        }
    }

}
