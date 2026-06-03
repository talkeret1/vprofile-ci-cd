package com.visualpathit.account.securityTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.RedirectStrategy;

import com.visualpathit.account.security.AuthenticationFailureHandler;

public class AuthenticationFailureHandlerTest {

        @InjectMocks
        private AuthenticationFailureHandler handler;

        @Mock
        private HttpServletRequest request;

        @Mock
        private HttpServletResponse response;

        @Mock
        private AuthenticationException exception;

        @Mock
        private RedirectStrategy redirectStrategy;

        @Before
        public void setup() {
                MockitoAnnotations.initMocks(this);

                when(request.getContextPath()).thenReturn("");

                // אם ה-handler injects strategy פנימי
                try {
                        java.lang.reflect.Field field = AuthenticationFailureHandler.class
                                        .getDeclaredField("redirectStrategy");
                        field.setAccessible(true);
                        field.set(handler, redirectStrategy);
                } catch (Exception ignored) {
                }
        }

        @Test
        public void shouldHandleFailure() throws Exception {

                doNothing().when(redirectStrategy).sendRedirect(any(), any(), any());

                handler.onAuthenticationFailure(request, response, exception);

                verify(redirectStrategy).sendRedirect(
                                any(HttpServletRequest.class),
                                any(HttpServletResponse.class),
                                contains("/login?error"));
        }
}