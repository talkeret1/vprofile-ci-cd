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

                securityService = new SecurityServiceImpl();

                inject("authenticationManager", authenticationManager);
                inject("userDetailsService", userDetailsService);

                SecurityContextHolder.clearContext();
        }

        private void inject(String field, Object value) {
                try {
                        var f = SecurityServiceImpl.class.getDeclaredField(field);
                        f.setAccessible(true);
                        f.set(securityService, value);
                } catch (Exception e) {
                        throw new RuntimeException(e);
                }
        }

        @Test
        public void shouldReturnNullWhenNoAuth() {
                SecurityContextHolder.clearContext();
                assertNull(securityService.findLoggedInUsername());
        }

        @Test
        public void shouldReturnNullWhenAuthNull() {
                SecurityContextHolder.getContext().setAuthentication(null);
                assertNull(securityService.findLoggedInUsername());
        }

        @Test
        public void shouldReturnUsernameFromContext() {

                User userDetails = new User("john", "pass", Collections.emptyList());

                var token = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                token.setDetails(userDetails);

                SecurityContextHolder.getContext().setAuthentication(token);

                assertEquals("john", securityService.findLoggedInUsername());
        }

        @Test
        public void shouldReturnNullWhenPrincipalNotUserDetails() {

                Authentication auth = mock(Authentication.class);
                when(auth.getPrincipal()).thenReturn("not-user");

                SecurityContextHolder.getContext().setAuthentication(auth);

                assertNull(securityService.findLoggedInUsername());
        }

        @Test
        public void shouldAutoLoginSuccess() {

                User userDetails = new User("john", "pass", Collections.emptyList());

                when(userDetailsService.loadUserByUsername("john")).thenReturn(userDetails);

                Authentication auth = mock(Authentication.class);
                when(authenticationManager.authenticate(any())).thenReturn(auth);
                when(auth.isAuthenticated()).thenReturn(true);

                assertTrue(securityService.autologin("john", "pass"));
        }

        @Test
        public void shouldAutoLoginFail() {

                User userDetails = new User("john", "pass", Collections.emptyList());

                when(userDetailsService.loadUserByUsername("john")).thenReturn(userDetails);

                Authentication auth = mock(Authentication.class);
                when(authenticationManager.authenticate(any())).thenReturn(auth);
                when(auth.isAuthenticated()).thenReturn(false);

                assertFalse(securityService.autologin("john", "pass"));
        }

        @Test
        public void shouldAutoLoginFailWhenExceptionThrown() {

                when(userDetailsService.loadUserByUsername("john"))
                                .thenThrow(new RuntimeException("fail"));

                assertFalse(securityService.autologin("john", "pass"));
        }
}