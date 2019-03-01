package com.toulios.projects.movierama.security;

import com.toulios.projects.movierama.exceptions.ResourceNotFoundException;
import com.toulios.projects.movierama.model.User;
import com.toulios.projects.movierama.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    @Autowired
    UserRepository userRepository;

    /**
     * Authenticates the user by the username or email
     * @param usernameOrEmail
     * @return  UserDetails object, which Spring Security uses for performing various authentication and role based validations.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) {

        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail)
        );

        return UserPrincipal.create(user);
    }

    /**
     * Load the user by id
     *
     * @param id
     * @return  UserDetails object, which Spring Security uses for performing various authentication and role based validations.
     */
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(user);
    }
}