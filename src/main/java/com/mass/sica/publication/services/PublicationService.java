/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.publication.services;

import com.mass.sica.domaine.entities.Domaine;
import com.mass.sica.domaine.entities.Specialite;
import com.mass.sica.domaine.entities.TypePublication;
import com.mass.sica.domaine.repositories.IDomaineRepository;
import com.mass.sica.domaine.repositories.ISpecialiteRepository;
import com.mass.sica.domaine.repositories.ITypePublicationRepository;
import com.mass.sica.domaine.services.DomaineService;
import com.mass.sica.publication.entities.AnneeSoutenance;
import com.mass.sica.publication.entities.Auteur;
import com.mass.sica.publication.entities.Publication;
import com.mass.sica.publication.repositories.IAnneeSoutenanceRepository;
import com.mass.sica.publication.repositories.IPublicationRepository;
import com.mass.sica.publication.utils.PDFMerger;
import com.mass.sica.publication.utils.PUBTypeToCSLType;
import com.mass.sica.utils.APIMessage;
import com.mass.sica.utils.UtilsJob;
import com.mass.sica.utils.exception.BadRequestException;
import com.mass.sica.utils.services.FileStorageService;
import com.mass.sica.utils.services.UtilsService;
import com.mass.sica.utils.specification.GenericSpecification;
import com.mass.sica.utils.specification.SearchCriteria;
import de.undercouch.citeproc.CSL;
import de.undercouch.citeproc.csl.CSLItemData;
import de.undercouch.citeproc.csl.CSLItemDataBuilder;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.stereotype.Service;

/**
 *
 * @author Aristide MASSAGA
 */
@Service
public class PublicationService {

    @Autowired
    private IPublicationRepository repository;

    @Autowired
    private IAnneeSoutenanceRepository anneeRepository;

    @Autowired
    private ISpecialiteRepository specialiteRepository;

    @Autowired
    private ITypePublicationRepository typePubRepository;

    @Autowired
    private IDomaineRepository domaineRepository;

    @Autowired
    private AuteurService auteurService;

    @Autowired
    private DomaineService domaineService;

    @Autowired
    private UtilsService utilsService;

    @Autowired
    private FileStorageService storageService;

    public Publication nouvellePublication(Publication data) throws BadRequestException {

        data.setDomaine(domaineRepository.findById(data.getDomaine().getId()).orElse(null));
        if (data.getDomaine() == null) {
            throw new BadRequestException(APIMessage.DOMAINE_INVALIDE);
        }

        data.setSpecialite(specialiteRepository.findById(data.getSpecialite().getId()).orElse(null));
        if (data.getSpecialite() == null) {
            throw new BadRequestException(APIMessage.SPECIALITE_INVALIDE);
        }

        data.setType(typePubRepository.findById(data.getType().getId()).orElse(null));
        if (data.getType() == null) {
            throw new BadRequestException(APIMessage.TYPE_PUBLICATION_INVALIDE);
        }

        // mots url
        data.setUrl(UtilsJob.buildUrl(data.getMotscle()));

        // version
        data.setVersion(1);
        data.setAccepter(false);
        data.setNbVue(0);
        data.setNbDownload(0);

        // gestion de l'année de soutenance
        AnneeSoutenance annee = new AnneeSoutenance(data.getDateSoutenance());
        // on vérifie si cette année là était déjà présente en BD
        annee = anneeRepository.findByMoisAndAnnee(annee.getMois(), annee.getAnnee());

        data.setAnneeSoutenance(annee == null ? anneeRepository.save(new AnneeSoutenance(data.getDateSoutenance())) : annee);

        // construction des auteurs
        data.setAuteurs(data.getAuteurs().stream().map(auteur -> this.auteurService.createUpdateAuteur(auteur)).collect(Collectors.toList()));

        data = repository.save(data);

        // code
        data.setCode(UtilsJob.codePubGenerator(data.getId()));

        try {
            // fichier
            URL url = new URL(data.getFichier());
        } catch (MalformedURLException ex) {
            try {
                String fileName = data.getFichier();
                storageService.moveFilePublication(fileName, "publications", data.getId().toString());

                // faire la première page en image
                String image = storageService.pdfToImage("publications" + File.separator + data.getId(), data.getId().toString(), fileName);
                data.setImage(image);

                // dossier de la publication
                String dossier = "publications" + File.separator + data.getId() + File.separator;

                // ajout de la page de SICA
                String firstPage = storageService.makeSicaPubPage(data, dossier);

                // générattion de la page de garde SICA
                String fichier = storageService.mergeInto(dossier + data.getCode() + "-" + fileName, firstPage, dossier + fileName);
                data.setFichier(fichier);
            } catch (IOException ex1) {
                Logger.getLogger(PublicationService.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (Exception ex1) {
                Logger.getLogger(PublicationService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        data = repository.save(data);

        return data;
    }

    /**
     * rechercher de toutes les publications
     *
     * @param enAttente
     * @param pgble
     * @return
     */
    public Page<Publication> allPublications(Boolean enAttente, Pageable pgble) {
        if (enAttente) {
            return this.repository.findByPublishedTrueAndAccepterFalse(pgble);
        } else {
            return this.repository.findByPublishedTrue(pgble);
        }
    }

    public Page<Publication> publications(Pageable pgble) {
        return this.repository.findByPublishedTrueAndAccepterTrue(pgble);
    }

    public Publication publication(String idCode) {
        if (idCode.contains("SICA")) {
            return repository.findByCode(idCode).orElse(null);
        }

        return repository.findById(Long.valueOf(idCode)).orElse(null);
    }

    public Publication consulterPublication(String idCode) {
        Publication pub = idCode.contains("SICA") ? repository.findByCode(idCode).orElse(null) : repository.findById(Long.valueOf(idCode)).orElse(null);

        pub.setNbVue(pub.getNbVue() + 1);
        return repository.save(pub);
    }

    public List<Publication> publicationsByAuteur(Long idAuteur) {

        return repository.findByAuteursIdAndPublishedTrue(idAuteur);
    }

    /**
     * on récupère les publications que l'utilisateur connecté a créé
     *
     * @param creator
     * @param pgble
     * @return
     */
    public Page<Publication> publicationsByCreator(String creator, Pageable pgble) {

        return repository.findByCreatedByAndPublishedTrue(creator, pgble);
    }

    /**
     * Ce code n'est appelable que par le gestionnaire. Il permet d'activer une
     * publication
     *
     * @param idPub
     * @return
     */
    public Boolean activerPublication(Long idPub) throws BadRequestException {

        Publication pub = repository.getOne(idPub);

        if (pub.getAccepter() == true) {
            throw new BadRequestException(APIMessage.PUBLICATION_DEJA_PUBLIEE);
        }
        // mots url
        pub.setUrl(pub.getUrl() + "-" + idPub.toString());
        pub.setAccepter(Boolean.TRUE);
        pub.setDateAcceptation(new Date());

        // met à jours les statistiques du nombre de publication
        domaineService.setDomaineStat(pub.getSpecialite().getDomaine(), 1);
        domaineService.setTypePublicationStat(pub.getType(), 1);
        domaineService.setSpecialiteStat(pub.getSpecialite(), 1);

        // met à jour les statistiques sur la année de publication
        setAnneeSoutenanceStat(pub.getAnneeSoutenance(), 1);

        // citation
        pub.setCitation(PUBTypeToCSLType.citation(pub));
        /*CSLItemDataBuilder citebuilder = new CSLItemDataBuilder()
                .type(PUBTypeToCSLType.convert(pub.getType().getDesignation()))
                .title(pub.getTitre())
                .page(pub.getPage())
                .language(pub.getLangue())
                .URL(pub.getFichier())
                .keyword(pub.getMotscle())
                .eventPlace((pub.getUniversite() != null ? pub.getUniversite() : pub.getColloque()))
                .locator((pub.getUniversite() != null ? pub.getUniversite() : pub.getColloque()))
                .yearSuffix(String.valueOf(pub.getAnneeSoutenance().getAnnee()))
                .eventDate(pub.getAnneeSoutenance().getAnnee(), pub.getAnneeSoutenance().getMois());
        citebuilder.author(PUBTypeToCSLType.convert(pub.getAuteurs()));
        CSLItemData citebuild = citebuilder.build();
        try {
            String bibl = CSL.makeAdhocBibliography("apa", "text", citebuild).makeString();
            pub.setCitation(PUBTypeToCSLType.citation(pub));
        } catch (IOException ex) {
            throw new BadRequestException(APIMessage.ERREUR_INCONNUE);
        }*/

        repository.save(pub);
        return true;
    }

    public Boolean depublier(Long idPub) throws BadRequestException {

        Publication pub = repository.getOne(idPub);

        if (pub.getAccepter() == false) {
            throw new BadRequestException(APIMessage.PUBLICATION_PAS_PUBLIEE);
        }

        pub.setAccepter(Boolean.FALSE);
        pub.setDateAcceptation(null);

        // met à jours les statistiques du nombre de publication
        domaineService.setDomaineStat(pub.getSpecialite().getDomaine(), -1);
        domaineService.setTypePublicationStat(pub.getType(), -1);
        domaineService.setSpecialiteStat(pub.getSpecialite(), -1);

        // met à jour les statistiques sur la année de publication
        setAnneeSoutenanceStat(pub.getAnneeSoutenance(), -1);

        repository.save(pub);
        return true;
    }

    private int setAnneeSoutenanceStat(AnneeSoutenance annee, int add) {

        annee.setStats(annee.getStats() + add);

        anneeRepository.save(annee);

        return annee.getStats();
    }

    public Publication modifierPublication(Long id, Publication data) throws BadRequestException {

        Publication pub = repository.findById(id).orElse(null);

        if (pub == null) {
            throw new BadRequestException(APIMessage.PUBLICATION_INVALIDE);
        }

        pub.update(data);

        if (!Objects.equals(pub.getSpecialite().getId(), data.getSpecialite().getId())) {
            pub.setSpecialite(specialiteRepository.findById(data.getSpecialite().getId()).orElse(null));
            if (data.getSpecialite() == null) {
                throw new BadRequestException(APIMessage.SPECIALITE_INVALIDE);
            }
        }

        if (!Objects.equals(pub.getType().getId(), data.getType().getId())) {
            pub.setType(typePubRepository.findById(data.getType().getId()).orElse(null));
            if (data.getType() == null) {
                throw new BadRequestException(APIMessage.TYPE_PUBLICATION_INVALIDE);
            }
        }

        // mots clés
        pub.setUrl(UtilsJob.buildUrl(data.getMotscle()));

        // version
        pub.setVersion(pub.getVersion() + 1);

        // gestion de l'année de soutenance
        AnneeSoutenance annee = new AnneeSoutenance(data.getDateSoutenance());
        // on vérifie si cette année là était déjà présente en BD
        annee = anneeRepository.findByMoisAndAnnee(annee.getMois(), annee.getAnnee());
        pub.setAnneeSoutenance(annee == null ? anneeRepository.save(new AnneeSoutenance(data.getDateSoutenance())) : annee);

        // construction des auteurs
        pub.setAuteurs(data.getAuteurs().stream().map(auteur -> this.auteurService.createUpdateAuteur(auteur)).collect(Collectors.toList()));

        // dossier de la publication
        String dossier = "publications" + File.separator + data.getId() + File.separator;

        try {
            // fichier
            URL url = new URL(data.getFichier());
            pub.setFichier(data.getFichier());

            // on vérifie s'il faut refaire la première page
            String fileName = UtilsJob.getFileName(data.getFichier());
            if (fileName != null) {
                // ajout de la page de SICA
                String firstPage = storageService.makeSicaPubPage(data, dossier);

                // générattion de la page de garde SICA
                String fichier = storageService.mergeInto(dossier + data.getCode() + "-" + fileName, firstPage, dossier + fileName);
                pub.setFichier(fichier);
            }
        } catch (MalformedURLException ex) {
            try {
                String fileName = data.getFichier();

                storageService.moveFilePublication(fileName, "publications", data.getId().toString());

                // faire la première page en image
                String image = storageService.pdfToImage("publications" + File.separator + data.getId(), data.getId().toString(), fileName);
                pub.setImage(image);

                // ajout de la page de SICA
                String firstPage = storageService.makeSicaPubPage(data, dossier);

                // générattion de la page de garde SICA
                String fichier = storageService.mergeInto(dossier + data.getCode() + "-" + fileName, firstPage, dossier + fileName);
                pub.setFichier(fichier);

            } catch (IOException ex1) {
                Logger.getLogger(PublicationService.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (Exception ex1) {
                Logger.getLogger(PublicationService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (Exception ex) {
            Logger.getLogger(PublicationService.class.getName()).log(Level.SEVERE, null, ex);
        }

        // dépublier
        try {
            this.depublier(id);
            pub.setAccepter(Boolean.FALSE);
        } catch (BadRequestException e) {
        }

        return repository.save(pub);
    }

    /**
     * Ce code est appelé par la vue consulter et il permet d'afficher les
     * publications selon des critères prédéfinis
     *
     * @param params
     * @param pgble
     * @return
     */
    public Page<Publication> rechercher(Map<String, String> params, Pageable pgble) {
        Specification<Publication> result = new GenericSpecification<>(new SearchCriteria("published", ":", true));

        // recherche parmi les publications acceptées
        result = result.and(where((root, query, builder) -> {
            return builder.and(
                    builder.equal(root.get("accepter"), true)
            );
        }
        ));
        if (params.get("domaine") != null) {
            String[] param = params.get("domaine").split(",");

            result = result.and(where((root, query, builder) -> {

                final Join<Publication, Domaine> domaine = root.join("domaine", JoinType.LEFT);

                final List<Predicate> predicates = new ArrayList<>();

                for (String para : param) {
                    predicates.add(builder.equal(domaine.get("id"), Long.valueOf(para)));
                }

                return builder.or(predicates.toArray(new Predicate[predicates.size()]));
            }
            ));
        }

        if (params.get("specialite") != null) {
            String[] param = params.get("specialite").split(",");

            result = result.and(where((root, query, builder) -> {
                final Join<Publication, Specialite> specialite = root.join("specialite", JoinType.LEFT);
                final List<Predicate> predicates = new ArrayList<>();

                for (String para : param) {
                    predicates.add(builder.equal(specialite.get("id"), Long.valueOf(para)));
                }
                return builder.or(predicates.toArray(new Predicate[predicates.size()]));
            }
            ));
        }

        if (params.get("type") != null) {
            String[] param = params.get("type").split(",");

            result = result.and(where((root, query, builder) -> {
                final Join<Publication, TypePublication> type = root.join("type", JoinType.LEFT);
                final List<Predicate> predicates = new ArrayList<>();

                for (String para : param) {
                    predicates.add(builder.equal(type.get("id"), Long.valueOf(para)));
                }
                return builder.or(predicates.toArray(new Predicate[predicates.size()]));
            }
            ));

        }

        if (params.get("annee") != null) {
            String[] param = params.get("annee").split(",");

            result = result.and(where((root, query, builder) -> {
                final Join<Publication, AnneeSoutenance> type = root.join("anneeSoutenance", JoinType.LEFT);
                final List<Predicate> predicates = new ArrayList<>();

                for (String para : param) {
                    predicates.add(builder.equal(type.get("annee"), Integer.parseInt(para)));
                }
                return builder.or(predicates.toArray(new Predicate[predicates.size()]));
            }
            ));
        }

        if (params.get("search") != null) {
            if (params.get("critere").equals("*")) {

                result = result.and(where((root, query, builder) -> {
                    final List<Predicate> predicates = new ArrayList<>();

                    predicates.add(builder.like(builder.lower(root.get("titre")), "%" + params.get("search") + "%"));
                    predicates.add(builder.like(builder.lower(root.get("soustitre")), "%" + params.get("search") + "%"));
                    predicates.add(builder.like(builder.lower(root.get("motscle")), "%" + params.get("search") + "%"));
                    predicates.add(builder.like(builder.lower(root.get("resume")), "%" + params.get("search") + "%"));
                    predicates.add(builder.like(builder.lower(root.get("astract")), "%" + params.get("search") + "%"));
                    predicates.add(builder.like(builder.lower(root.get("code")), "%" + params.get("search") + "%"));
                    predicates.add(builder.like(builder.lower(root.get("optio")), "%" + params.get("search") + "%"));
                    predicates.add(builder.like(builder.lower(root.get("universite")), "%" + params.get("search") + "%"));

                    return builder.or(predicates.toArray(new Predicate[predicates.size()]));
                }
                ));

            } else {
                result = where(result)
                        .and(new GenericSpecification<>(
                                new SearchCriteria(params.get("critere"), "~", params.get("search"), null, "large")));
            }

        }

        return this.repository.findAll(result, pgble);
    }

    /**
     * supprimer une publication
     *
     * @param id
     * @return
     */
    public Boolean supprimerPublication(Long id) throws BadRequestException {
        Publication pub = repository.findById(id).orElse(null);

        if (pub == null) {
            throw new BadRequestException(APIMessage.PUBLICATION_INVALIDE);
        }

        if (pub.getAccepter()) {
            this.depublier(id);
        }

        pub.setPublished(false);

        repository.save(pub);
        return true;
    }

}
