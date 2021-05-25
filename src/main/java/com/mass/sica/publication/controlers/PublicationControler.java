/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.publication.controlers;

import com.mass.sica.publication.entities.AnneeSoutenance;
import com.mass.sica.publication.entities.Publication;
import com.mass.sica.publication.projections.AnneeSoutenanceProjection;
import com.mass.sica.publication.repositories.IAnneeSoutenanceRepository;
import com.mass.sica.publication.services.PublicationService;
import com.mass.sica.utils.APIMessage;
import com.mass.sica.utils.ApiResponse;
import com.mass.sica.utils.UtilsJob;
import com.mass.sica.utils.exception.BadRequestException;
import java.security.Principal;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Aristide MASSAGA
 */
@RestController
@RequestMapping("/api/")
public class PublicationControler {

    @Autowired
    private IAnneeSoutenanceRepository anneeRepository;

    @Autowired
    private PublicationService service;

    @Secured("ROLE_ADMIN")
    @GetMapping("publication/all")
    public ApiResponse<Page<Publication>> allPublications(@RequestParam(value = "enAttente", required = false) Boolean enAttente, Pageable pgble) {

        return new ApiResponse<>(true, service.allPublications(enAttente, pgble));
    }

    @GetMapping("publication/")
    public ApiResponse<Page<Publication>> publications(Pageable pgble) {

        return new ApiResponse<>(true, service.publications(pgble));
    }

    @GetMapping("publication/consulter")
    public ApiResponse<Page<Publication>> consulter(HttpServletRequest request, Pageable pgble) {
        return new ApiResponse<>(true, service.rechercher(UtilsJob.convertParamsRequest(request.getParameterMap()), pgble));
    }

    @GetMapping("publication/rechercher")
    public ApiResponse<Page<Publication>> rechercher(HttpServletRequest request, Pageable pgble) {
        return new ApiResponse<>(true, service.rechercher(UtilsJob.convertParamsRequest(request.getParameterMap()), pgble));
    }

    @PostMapping("rest/publication/")
    @Transactional
    public ApiResponse<Boolean> nouvellePublication(@Valid @RequestBody Publication publication) {

        try {
            service.nouvellePublication(publication);
            return new ApiResponse<>(true, true);
        } catch (BadRequestException ex) {
            return new ApiResponse<>(false, ex.getMessage());
        }
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("rest/publication/valider/{id}")
    @Transactional
    public ApiResponse<Boolean> activerPublication(@PathVariable(value = "id") Long id) {

        try {
            return new ApiResponse<>(true, APIMessage.PUBLICATION_PUBLIEE, service.activerPublication(id));
        } catch (BadRequestException ex) {
            return new ApiResponse<>(false, ex.getMessage());
        }
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("rest/publication/depublier/{id}")
    @Transactional
    public ApiResponse<Boolean> depublierPublication(@PathVariable(value = "id") Long id) {

        try {
            return new ApiResponse<>(true, APIMessage.PUBLICATION_DEPUBLIEE, service.depublier(id));
        } catch (BadRequestException ex) {
            return new ApiResponse<>(false, ex.getMessage());
        }
    }

    @GetMapping("publication/{id}")
    public ApiResponse<Publication> publication(@PathVariable(value = "id") String idCode) {

        return new ApiResponse<>(true, service.publication(idCode));
    }

    @GetMapping("publication/consulter/{id}")
    public ApiResponse<Publication> consulterPublication(@PathVariable(value = "id") String idCode) {

        return new ApiResponse<>(true, service.consulterPublication(idCode));
    }

    @GetMapping("publication/by-auteur/{id}")
    public ApiResponse<List<Publication>> publicationsByAuteur(@PathVariable(value = "id") Long idAuteur) {

        return new ApiResponse<>(true, service.publicationsByAuteur(idAuteur));
    }

    @GetMapping("publication/by-creator")
    public ApiResponse<Page<Publication>> publicationsByCreator(Principal principal, Pageable pgble) {

        return new ApiResponse<>(true, service.publicationsByCreator(principal.getName(), pgble));
    }

    @PutMapping("rest/publication/{id}")
    @Transactional
    public ApiResponse<Boolean> modifierPublication(@Valid @RequestBody Publication publication, @PathVariable(value = "id") Long id) {

        try {
            service.modifierPublication(id, publication);
            return new ApiResponse<>(true, true);
        } catch (BadRequestException ex) {
            return new ApiResponse<>(false, ex.getMessage());
        }
    }

    @DeleteMapping("rest/publication/{id}")
    @Transactional
    public ApiResponse<Boolean> supprimerPublication(@PathVariable(value = "id") Long id) {
        try {
            return new ApiResponse<>(true, service.supprimerPublication(id));
        } catch (BadRequestException ex) {
            return new ApiResponse<>(false, ex.getMessage());
        }
    }

    @GetMapping("publication/annees-publications")
    public ApiResponse<List<AnneeSoutenance>> anneesPublications() {
        return new ApiResponse<>(true, this.anneeRepository.findAll());
    }

    @GetMapping("publication/annees-publications/toconsult")
    public ApiResponse<List<AnneeSoutenanceProjection>> anneesPublicationsToconsult() {
        return new ApiResponse<>(true, this.anneeRepository.selectOrderByAnneeDesc());
    }

    @GetMapping("publication/annees-publications/by-annee")
    public ApiResponse<List<AnneeSoutenance>> anneesPublicationsByAnnee() {
        return new ApiResponse<>(true, this.anneeRepository.findByAnneeOrderByMoisAsc(Calendar.getInstance().get(Calendar.YEAR)));
    }
}
