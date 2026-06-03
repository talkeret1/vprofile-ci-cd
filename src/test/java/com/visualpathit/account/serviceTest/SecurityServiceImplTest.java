package com.visualpathit.account.serviceTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.visualpathit.account.service.SecurityServiceImpl;

public class SecurityServiceImplTest {

        @InjectMocks
        private SecurityServiceImpl securityService;

        @Mock
        private AuthenticationManager authenticationManager;

        @Mock
        private UserDetailsService userDetailsService;

        @Before
        public void setup() {
                MockitoAnnotations.initMocks(this);
                SecurityContextHolder.clearContext();
        }

        @Test
        public void shouldReturnNullWhenNoUserLoggedIn() {

                SecurityContextHolder.clearContext();

                assertNull(securityService.findLoggedInUsername());
        }

        @Test
        public void shouldAutoLoginSuccessfully() {

                User userDetails = new User(
                                "john",
                                "password",
                                Collections.emptyList());

                when(userDetailsService.loadUserByUsername("john"))
                                .thenReturn(userDetails);

                Authentication mockAuth = mock(Authentication.class);

                when(authenticationManager.authenticate(any()))
                                .thenReturn(mockAuth);

                when(mockAuth.isAuthenticated()).thenReturn(true);

                boolean result = securityService.autologin("john", "password");

                assertTrue(result);
        }

        @Test
        public void shouldFindLoggedInUsername() {

                User userDetails = new User(
                                "john",
                                "password",
                                Collections.emptyList());

                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                token.setDetails(userDetails);

                SecurityContextHolder.getContext()
                                .setAuthentication(token);

                assertEquals(
                                "john",
                                securityService.findLoggedInUsername());
        }
}