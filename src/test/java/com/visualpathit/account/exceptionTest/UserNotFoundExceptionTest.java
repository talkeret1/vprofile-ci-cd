package com.visualpathit.account.exceptionTest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.visualpathit.account.UserNotFoundException;

public class UserNotFoundExceptionTest {

    @Test
    public void shouldCreateExceptionWithMessage() {

        UserNotFoundException ex = new UserNotFoundException("user not found");

        assertEquals("user not found", ex.getMessage());
    }
}