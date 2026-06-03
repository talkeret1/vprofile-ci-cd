package com.visualpathit.account.securityTest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.AuthenticationException;

import com.visualpathit.account.security.AuthenticationFailureHandler;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class AuthenticationFailureHandlerTest {

        @InjectMocks
        private AuthenticationFailureHandler handler;

        @Mock
        private HttpServletRequest request;

        @Mock
        private HttpServletResponse response;

        @Mock
        private HttpSession session;

        @Mock
        private AuthenticationException exception;

        @Before
        public void setup() {
                MockitoAnnotations.initMocks(this);

                when(request.getSession()).thenReturn(session);
                when(request.getContextPath()).thenReturn("");
        }

        @Test
        public void shouldHandleFailure() throws Exception {

                handler.onAuthenticationFailure(request, response, exception);

                verify(request, atLeastOnce()).getSession();
                verify(response, atLeastOnce()).encodeRedirectURL(anyString());

                assertTrue(true);
        }
}