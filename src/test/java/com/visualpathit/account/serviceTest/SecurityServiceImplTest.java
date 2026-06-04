package com.visualpathit.account.serviceTest;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
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

        private SecurityServiceImpl securityService;

        @Mock
        private AuthenticationManager authenticationManager;

        @Mock
        private UserDetailsService userDetailsService;

        @Before
        public void setup() {
                MockitoAnnotations.initMocks(this);
                SecurityContextHolder.clearContext();

                securityService = new SecurityServiceImpl();

                inject("authenticationManager", authenticationManager);
                inject("userDetailsService", userDetailsService);
        }

        private void inject(String field, Object value) {
                try {
                        java.lang.reflect.Field f = SecurityServiceImpl.class.getDeclaredField(field);
                        f.setAccessible(true);
                        f.set(securityService, value);
                } catch (Exception e) {
                        throw new RuntimeException(e);
                }
        }

        @Test
        public void shouldReturnNullWhenContextNull() {
                SecurityContextHolder.setContext(null);
                assertNull(securityService.findLoggedInUsername());
        }

        @Test
        public void shouldReturnNullWhenAuthNull() {
                SecurityContextHolder.clearContext();
                assertNull(securityService.findLoggedInUsername());
        }

        @Test
        public void shouldReturnNullWhenDetailsNotUserDetails() {
                Authentication auth = mock(Authentication.class);
                when(auth.getDetails()).thenReturn("not-user-details");

                SecurityContextHolder.getContext().setAuthentication(auth);

                assertNull(securityService.findLoggedInUsername());
        }

        @Test
        public void shouldReturnUsernameFromSecurityContext() {

                User userDetails = new User("john", "pass", Collections.emptyList());

                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null,
                                userDetails.getAuthorities());

                token.setDetails(userDetails);

                SecurityContextHolder.getContext().setAuthentication(token);

                assertEquals("john", securityService.findLoggedInUsername());
        }

        @Test
        public void shouldAutoLoginSuccessfully() {

                User userDetails = new User("john", "password", Collections.emptyList());

                when(userDetailsService.loadUserByUsername("john")).thenReturn(userDetails);

                Authentication auth = mock(Authentication.class);
                when(authenticationManager.authenticate(any())).thenReturn(auth);
                when(auth.isAuthenticated()).thenReturn(true);

                boolean result = securityService.autologin("john", "password");

                assertTrue(result);
        }

        @Test
        public void shouldFailAutoLoginWhenNotAuthenticated() {

                User userDetails = new User("john", "password", Collections.emptyList());

                when(userDetailsService.loadUserByUsername("john")).thenReturn(userDetails);

                Authentication auth = mock(Authentication.class);
                when(authenticationManager.authenticate(any())).thenReturn(auth);
                when(auth.isAuthenticated()).thenReturn(false);

                boolean result = securityService.autologin("john", "password");

                assertFalse(result);
        }
}