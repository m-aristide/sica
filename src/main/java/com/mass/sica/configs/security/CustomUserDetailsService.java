package com.mass.sica.configs.security;

import com.mass.sica.utilisateur.entities.Utilisateur;
import com.mass.sica.utilisateur.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UtilisateurRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        // Let people login with either username or email
        Utilisateur user = userRepository.findByUsername(username)
                .orElseThrow(()
                        -> new UsernameNotFoundException("User not found with username or email : " + username)
                );

        return user;
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        Utilisateur user = userRepository.findById(id).orElse(null);

        return user;
    }
}
