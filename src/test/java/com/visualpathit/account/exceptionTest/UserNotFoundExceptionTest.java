package com.visualpathit.account.exceptionTest;

import org.junit.Test;

import com.visualpathit.account.exception.UserNotFoundException;

import static org.junit.Assert.*;

public class UserNotFoundExceptionTest {

    @Test
    public void shouldCreateExceptionWithMessage() {

        UserNotFoundException ex = new UserNotFoundException("User not found");

        assertNotNull(ex);
        assertEquals("User not found", ex.getMessage());
    }
}