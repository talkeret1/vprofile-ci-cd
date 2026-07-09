package com.visualpathit.account.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

    @Override
    public String findLoggedInUsername() {

        if (SecurityContextHolder.getContext() == null) {
            return null;
        }

        Object auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            return null;
        }

        Object principal = ((org.springframework.security.core.Authentication) auth).getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }

        return null;
    }

    @Override
    public boolean autologin(final String username, final String password) {

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    password,
                    userDetails.getAuthorities());

            AuthenticationManager manager = this.authenticationManager;

            var authResult = manager.authenticate(token);

            if (authResult != null && authResult.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authResult);
                logger.debug(String.format("Auto login %s successfully!", username));
                return true;
            }

        } catch (Exception e) {
            logger.debug(String.format("Auto login %s failed!", username));
            return false;
        }

        logger.debug(String.format("Auto login %s failed!", username));
        return false;
    }
}