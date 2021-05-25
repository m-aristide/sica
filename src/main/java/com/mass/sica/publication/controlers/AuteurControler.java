/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.publication.controlers;

import com.mass.sica.utilisateur.repositories.UtilisateurRepository;
import com.mass.sica.publication.entities.Auteur;
import com.mass.sica.publication.repositories.IAuteurRepository;
import com.mass.sica.utilisateur.entities.Utilisateur;
import com.mass.sica.utils.ApiResponse;
import com.mass.sica.utils.specification.GenericSpecification;
import com.mass.sica.utils.specification.SearchCriteria;
import java.security.Principal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Aristide MASSAGA
 */
@RestController
@RequestMapping("/api/")
public class AuteurControler {

    @Autowired
    private IAuteurRepository repository;

    @Autowired
    private UtilisateurRepository userRepository;

    @GetMapping("auteurs/")
    public ApiResponse<List<Auteur>> auteurs() {
        return new ApiResponse<>(true, this.repository.findByPublishedTrue());
    }

    @GetMapping("auteurs/{id}")
    public ApiResponse<Auteur> auteur(@PathVariable(value = "id") Long id) {
        return new ApiResponse<>(true, this.repository.findById(id).orElse(null));
    }

    @GetMapping("auteurs/pagesearch")
    public ApiResponse<Page<Auteur>> pageSearch(@RequestParam(value = "search") String search, Pageable pgble) {

        Specification<Auteur> result = new GenericSpecification<>(new SearchCriteria("published", ":", true));

        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            result = where(result)
                    .and(new GenericSpecification<>(
                            new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3))));
        }
        return new ApiResponse<>(true, this.repository.findAll(result, pgble));
    }

    @GetMapping(value = "auteurs/by-lettre/{lettre}")
    public ApiResponse<List<Auteur>> auteursByLetter(@PathVariable(value = "lettre") String lettre) {

        return new ApiResponse<>(true, this.repository.findByPublishedTrueAndNomLike(lettre + "%"));
    }

    @GetMapping(value = "rest/auteurs/find-me-as-author")
    public ApiResponse<Auteur> findMeAsAuthor(Principal principal) {

        Utilisateur user = userRepository.findByUsername(principal.getName()).orElse(null);

        Auteur auteur = this.repository.findByEmail(user.getEmail());

        if (auteur != null) {
            return new ApiResponse<>(true, auteur);
        }

        auteur = this.repository.findByNom(user.getNom());
        if (auteur != null && user.getPrenom().equals(auteur.getPrenom())) {
            return new ApiResponse<>(true, auteur);
        }

        auteur = new Auteur();
        auteur.setNom(user.getNom());
        auteur.setEmail(user.getEmail());
        auteur.setPrenom(user.getPrenom());
        auteur.setTelephone(user.getTelephone());
        
        return new ApiResponse<>(true, auteur);
    }
}
