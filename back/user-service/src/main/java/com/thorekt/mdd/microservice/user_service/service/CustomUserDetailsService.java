package com.thorekt.mdd.microservice.user_service.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.thorekt.mdd.microservice.user_service.model.User;
import com.thorekt.mdd.microservice.user_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Custom implementation of UserDetailsService to load user details from the
 * database.
 * This service is used by Spring Security for authentication and authorization.
 * 
 * @author thorekt
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Load a user by username (in this case, email) from the database and return a
     * UserDetails object.
     * 
     * @param username the username (email) of the user to load
     * @return UserDetails object containing user information and authorities
     * @throws UsernameNotFoundException if the user is not found in the database
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find the user by email (which is used as username because of
        // UserDetailsService implementation)
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // For simplicity, all users have the ROLE_USER authority here because we have
        // no roles in the DBUser entity
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }

}
