/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.configs.auth;

import com.mass.sica.configs.payloads.ChangePassword;
import com.mass.sica.configs.security.JwtTokenProvider;
import com.mass.sica.notification.entity.Mail;
import com.mass.sica.notification.service.NotificationService;
import com.mass.sica.utilisateur.entities.Utilisateur;
import com.mass.sica.utilisateur.repositories.UtilisateurRepository;
import com.mass.sica.utils.APIMessage;
import com.mass.sica.utils.ApiResponse;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 *
 * @author Aristide MASSAGA
 */
@RestController
@RequestMapping("/api")
public class AuthController {

    @Value("${app.mailhost}")
    private String host;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    private static final Random RANDOM = new SecureRandom();

    @Transactional
    @PostMapping("/auth/nouvel-utilisateur")
    public ApiResponse<Boolean> nouvelUtilisateur(@Valid @RequestBody Utilisateur user) {

        // utilisateur existe
        if (this.utilisateurRepository.findByUsername(user.getUsername()).isPresent()) {
            return new ApiResponse<>(false, APIMessage.UTILISATEUR_EXISTE, false);
        }

        //  création de l'utilisateur
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        user.setCreatedAt(new Date());
        user.setActive(String.valueOf(new Date().getTime()));
        user = this.utilisateurRepository.save(user);

        // envoi mail à l'utilisateur
        Mail mail = new Mail(user.getEmail(), "Confirmation du compte", null);
        Context context = new Context();
        context.setVariable("utilisateur", user);
        context.setVariable("host", host);
        context.setVariable("destinataire", mail.getDestinataire());
        mail.setBody(templateEngine.process("mails/confirmation", context));
        this.notificationService.mail(mail);

        return new ApiResponse<>(true, APIMessage.UTILISATEUR_CREE, true);
    }

    @Transactional
    @GetMapping("/auth/confirmation")
    public ApiResponse<String> confirmationUtilisateur(@RequestParam(name = "code") String etat) {

        Optional<Utilisateur> user = this.utilisateurRepository.findByActive(etat);

        // code etat invalide
        if (!user.isPresent()) {
            return new ApiResponse<>(false, APIMessage.UTILISATEUR_INVALIDE, null);
        }

        user.get().setActive("ACTIVE");
        this.utilisateurRepository.save(user.get());

        return new ApiResponse<>(true, APIMessage.UTILISATEUR_ACTIVE, tokenProvider.generateToken(user.get().getUsername()));
    }

    @Transactional
    @PostMapping("/auth/connexion")
    public ApiResponse<String> connexion(@RequestBody Utilisateur data) {

        Optional<Utilisateur> user = this.utilisateurRepository.findByUsername(data.getUsername());

        // code etat invalide
        if (!user.isPresent()) {
            return new ApiResponse<>(false, APIMessage.USERNAME_PASSWORD_INVALIDE, null);
        }

        if (!"ACTIVE".equals(user.get().getActive())) {
            return new ApiResponse<>(false, APIMessage.USERNAME_PASSWORD_INVALIDE, null);
        }

        if (!passwordEncoder.matches(data.getPassword(), user.get().getPassword())) {

            return new ApiResponse<>(false, APIMessage.USERNAME_PASSWORD_INVALIDE, null);
        }

        return new ApiResponse<>(true, APIMessage.CONNEXION_REUSSIE, tokenProvider.generateToken(user.get().getUsername()));
    }

    @Transactional
    @GetMapping("/auth/mdp-oublie")
    public ApiResponse<String> mdpOublie(@RequestParam(name = "email") String email) {

        Optional<Utilisateur> user = this.utilisateurRepository.findByEmail(email);

        // code etat invalide
        if (!user.isPresent()) {
            return new ApiResponse<>(false, "Adresse e-mail invalide", null);
        }

        // reinitialisation
        String password = AuthController.generateRandomPassword();
        user.get().setPassword(passwordEncoder.encode(password));
        this.utilisateurRepository.save(user.get());

        Mail mail = new Mail(user.get().getEmail(), "Mot de passe oublié", null);
        Context context = new Context();
        context.setVariable("utilisateur", user.get());
        context.setVariable("password", password);
        context.setVariable("destinataire", mail.getDestinataire());
        mail.setBody(templateEngine.process("mails/mdpoublie", context));
        this.notificationService.mail(mail);

        return new ApiResponse<>(true, APIMessage.CONNEXION_REUSSIE, tokenProvider.generateToken(user.get().getUsername()));
    }

    @GetMapping("/rest/utilisateur")
    public ApiResponse<Utilisateur> utilisateur() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // on vérifie si la personne est connectée
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            Utilisateur user = utilisateurRepository.findByUsername(authentication.getName()).orElse(null);
            return new ApiResponse<>(true, user);
        }

        return new ApiResponse<>(false, APIMessage.UTILISATEUR_NON_AUTHENTIFIE);
    }

    /**
     * Generate a random String suitable for use as a temporary password.
     *
     * @return String suitable for use as a temporary password
     * @since 2.4
     */
    private static String generateRandomPassword() {
        // Pick from some letters that won't be easily mistaken for each
        // other. So, for example, omit o O and 0, 1 l and L.
        String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ0123456789";

        String pw = "";
        for (int i = 0; i < 10; i++) {
            int index = (int) (RANDOM.nextDouble() * letters.length());
            pw += letters.substring(index, index + 1);
        }
        return pw;
    }

    @Transactional
    @PutMapping("/rest/utilisateur/{userName}")
    public ApiResponse<Boolean> modifierUtilisateur(@RequestBody Utilisateur data, @PathVariable(value = "userName") String username) {

        Utilisateur user = this.utilisateurRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return new ApiResponse<>(false, APIMessage.UTILISATEUR_INVALIDE, false);
        }

        user.update(data);
        this.utilisateurRepository.save(user);

        return new ApiResponse<>(true, true);
    }

    @Transactional
    @PutMapping("/rest/utilisateur-pass/{userName}")
    public ApiResponse<Boolean> modifierPasswordUtilisateur(@RequestBody ChangePassword data, @PathVariable(value = "userName") String username) {

        Utilisateur user = this.utilisateurRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return new ApiResponse<>(false, APIMessage.UTILISATEUR_INVALIDE, false);
        }

        if (!this.passwordEncoder.matches(data.getPassword(), user.getPassword())) {
            return new ApiResponse<>(false, APIMessage.PASSWORD_INVALIDE, false);
        }

        user.setPassword(passwordEncoder.encode(data.getNewpassword()));
        this.utilisateurRepository.save(user);

        return new ApiResponse<>(true, true);
    }

}
