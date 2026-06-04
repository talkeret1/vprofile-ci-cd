package com.visualpathit.account.configTest;

import com.visualpathit.account.exception.GlobalExceptionHandler;
import com.visualpathit.account.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class GlobalExceptionHandlerTest {

    @Test
    public void shouldHandleUserNotFoundException() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        HttpServletRequest request = mock(HttpServletRequest.class);

        UserNotFoundException ex = new UserNotFoundException("User not found");

        String result = handler.handleUserNotFoundException(ex, request);

        assertEquals("forward:/WEB-INF/views/error/404.jsp", result);
        verify(request).setAttribute("errorMessage", "User not found");
    }

    @Test
    public void shouldHandleBadCredentialsException() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        HttpServletRequest request = mock(HttpServletRequest.class);

        BadCredentialsException ex = new BadCredentialsException("Bad credentials");

        String result = handler.handleBadCredentialsException(ex, request);

        assertEquals("forward:/WEB-INF/views/error/500.jsp", result);
        verify(request).setAttribute("errorMessage", "Invalid username or password.");
    }

    @Test
    public void shouldHandleGenericException() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        HttpServletRequest request = mock(HttpServletRequest.class);

        Exception ex = new Exception("Something went wrong");

        String result = handler.handleGenericException(ex, request);

        assertEquals("forward:/WEB-INF/views/error/500.jsp", result);
        verify(request).setAttribute("errorMessage",
                "An unexpected error occurred. Please try again later.");
    }

    // 🔥 EXTRA EDGE CASE (helps Sonar coverage stability)
    @Test
    public void shouldStillReturnErrorPageWhenMessageIsNull() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        HttpServletRequest request = mock(HttpServletRequest.class);

        UserNotFoundException ex = new UserNotFoundException(null);

        String result = handler.handleUserNotFoundException(ex, request);

        assertEquals("forward:/WEB-INF/views/error/404.jsp", result);
        verify(request).setAttribute("errorMessage", null);
    }
}