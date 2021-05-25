/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.magazine.controler;

import com.mass.sica.magazine.entities.Videotheque;
import com.mass.sica.magazine.repository.IVideothequeRepository;
import com.mass.sica.utils.APIMessage;
import com.mass.sica.utils.ApiResponse;
import com.mass.sica.utils.exception.BadRequestException;
import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Aristide MASSAGA
 */
@RestController
@RequestMapping("/api")
public class VideothequeControler {

    @Autowired
    private IVideothequeRepository videothequeRepository;

    @Secured("ROLE_ADMIN")
    @Transactional
    @PostMapping("/rest/videotheque/")
    public ApiResponse<Boolean> nouvelleVideotheque(@Valid @RequestBody Videotheque videotheque) {
        this.videothequeRepository.save(videotheque);
        return new ApiResponse<>(true, true);
    }

    @GetMapping("/videotheque/")
    public ApiResponse<List<Videotheque>> videotheques() {

        return new ApiResponse<>(true, this.videothequeRepository.findByPublishedTrue());
    }

    @GetMapping("/videotheque/page/")
    public ApiResponse<Page<Videotheque>> pageVideos(Pageable pgble) {
        return new ApiResponse<>(true, this.videothequeRepository.findAll(pgble));
    }
    
    @GetMapping("/videotheque/{id}")
    public ApiResponse<Videotheque> uneVideotheque(@PathVariable(value = "id") Long idVideotheque) {

        return new ApiResponse<>(true, this.videothequeRepository.findById(idVideotheque).get());
    }

    @Secured("ROLE_ADMIN")
    @Transactional
    @PutMapping("/rest/videotheque/{id}")
    public ApiResponse<Boolean> modifierDomaine(@Valid @RequestBody Videotheque data, @PathVariable(value = "id") Long idVideotheque) {
        try {
            Videotheque videotheque = this.videothequeRepository.findById(idVideotheque).orElse(null);
            if (videotheque == null) {
                throw new BadRequestException(APIMessage.VIDEO_INVALIDE);
            }
            videotheque.update(data);
            this.videothequeRepository.save(videotheque);
            return new ApiResponse<>(true, true);
        } catch (BadRequestException ex) {
            return new ApiResponse<>(false, ex.getMessage());
        }
    }

    @Secured("ROLE_ADMIN")
    @Transactional
    @DeleteMapping("/rest/videotheque/{id}")
    public ApiResponse<Boolean> supprimerVideotheque(@PathVariable(value = "id") Long idVideotheque) {
        try {
            Videotheque videotheque = this.videothequeRepository.findById(idVideotheque).orElse(null);
            if (videotheque == null) {
                throw new BadRequestException(APIMessage.VIDEO_INVALIDE);
            }
            videotheque.setPublished(false);
            this.videothequeRepository.save(videotheque);
            return new ApiResponse<>(true, true);
        } catch (BadRequestException ex) {
            return new ApiResponse<>(false, ex.getMessage());
        }
    }
}
