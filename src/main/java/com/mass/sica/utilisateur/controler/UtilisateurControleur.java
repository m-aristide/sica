/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.utilisateur.controler;

import com.mass.sica.domaine.entities.Domaine;
import com.mass.sica.publication.repositories.IPublicationRepository;
import com.mass.sica.utilisateur.entities.Bibliotheque;
import com.mass.sica.utilisateur.entities.Utilisateur;
import com.mass.sica.utilisateur.projections.BibliothequeProjection;
import com.mass.sica.utilisateur.repositories.IBibliothequeRepository;
import com.mass.sica.utilisateur.repositories.UtilisateurRepository;
import com.mass.sica.utils.APIMessage;
import com.mass.sica.utils.ApiResponse;
import java.security.Principal;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Aristide MASSAGA
 */
@RestController
@RequestMapping("/api/rest/utilisateur")
public class UtilisateurControleur {

    @Autowired
    private IPublicationRepository pubRepository;

    @Autowired
    private IBibliothequeRepository bibRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @PostMapping("/collections")
    public ApiResponse<Bibliotheque> collection(@Valid @RequestBody Bibliotheque bib, Principal principal) {

        if (bib.getId() != null) {
            Bibliotheque b = bibRepository.findByIdAndCreatedByAndPublishedTrue(bib.getId(), principal.getName());
            if (b == null) {
                return new ApiResponse(false, APIMessage.ERREUR_BIBLIOTHEQUE);
            }
            b.update(bib);
            return new ApiResponse(true, bibRepository.save(b));
        }

        return new ApiResponse(true, bibRepository.save(bib));
    }

    @GetMapping("/collections")
    public ApiResponse<List<BibliothequeProjection>> collections(Principal principal) {
        return new ApiResponse(true, bibRepository.findByCreatedByAndPublishedTrue(principal.getName()));
    }

    @GetMapping("/collections/{id}")
    public ApiResponse<Bibliotheque> collection(@PathVariable(value = "id") Long id, Principal principal) {
        return new ApiResponse(true, bibRepository.findByIdAndCreatedByAndPublishedTrue(id, principal.getName()));
    }

    @PostMapping("/collections/add-pub-bib")
    public ApiResponse<Boolean> addPubBib(@RequestParam(value = "pub") Long idPub, @RequestParam(value = "bib") Long idBib, Principal principal) {

        Bibliotheque bib = bibRepository.findByIdAndCreatedByAndPublishedTrue(idBib, principal.getName());

        if (bib == null) {
            return new ApiResponse(false, APIMessage.ERREUR_BIBLIOTHEQUE);
        }

        bib.getPublications().add(pubRepository.findById(idPub).get());
        bibRepository.save(bib);

        return new ApiResponse(true, APIMessage.PUB_AJOUTEE_BIBLIOTHEQUE, true);
    }

    @DeleteMapping("/collections/add-pub-bib")
    public ApiResponse<Boolean> removePubBib(@RequestParam(value = "pub") Long idPub, @RequestParam(value = "bib") Long idBib, Principal principal) {

        Bibliotheque bib = bibRepository.findByIdAndCreatedByAndPublishedTrue(idBib, principal.getName());

        if (bib == null) {
            return new ApiResponse(false, APIMessage.ERREUR_BIBLIOTHEQUE);
        }

        bib.getPublications().remove(pubRepository.findById(idPub).get());
        bibRepository.save(bib);

        return new ApiResponse(true, true);
    }

    @DeleteMapping("/collections/{id}")
    public ApiResponse<Boolean> deleteCollection(@PathVariable(value = "id") Long id, Principal principal) {
        Bibliotheque bib = bibRepository.findByIdAndCreatedByAndPublishedTrue(id, principal.getName());
        if (bib == null) {
            return new ApiResponse(false, APIMessage.ERREUR_BIBLIOTHEQUE);
        }
        bib.setPublished(false);
        bibRepository.save(bib);
        return new ApiResponse(true, true);
    }

    @GetMapping("/all")
    public ApiResponse<Page<Utilisateur>> utilisateurs(@RequestParam(value = "admin", required = false) Boolean admin, Pageable pgble) {
        return new ApiResponse(true, utilisateurRepository.findByActiveAndRole("ACTIVE", (admin ? "ROLE_ADMIN" : "ROLE_USER"), pgble));
    }

    @DeleteMapping("/all/{id}")
    public ApiResponse<Boolean> removeUtilisateur(@PathVariable(value = "id") Long id, Principal principal) {
        Utilisateur user = utilisateurRepository.findById(id).orElse(null);
        if (user == null) {
            return new ApiResponse(false, APIMessage.UTILISATEUR_INVALIDE);
        }
        user.setActive("FALSE");
        utilisateurRepository.save(user);
        return new ApiResponse(true,true);

    }

}
