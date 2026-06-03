package com.visualpathit.account.validatorTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import com.visualpathit.account.model.User;
import com.visualpathit.account.service.UserService;
import com.visualpathit.account.validator.UserValidator;

public class UserValidatorTest {

    private UserValidator validator;

    @Mock
    private UserService userService;

    @Before
    public void setup() throws Exception {

        MockitoAnnotations.initMocks(this);

        validator = new UserValidator();

        Field field = UserValidator.class.getDeclaredField("userService");
        field.setAccessible(true);
        field.set(validator, userService);
    }

    @Test
    public void shouldSupportUserClass() {
        assertTrue(validator.supports(User.class));
        assertFalse(validator.supports(String.class));
    }

    @Test
    public void shouldValidateValidUser() {

        User user = new User();
        user.setUsername("validuser");
        user.setPassword("password123");
        user.setPasswordConfirm("password123");

        when(userService.findByUsername("validuser"))
                .thenReturn(null);

        Errors errors = new BeanPropertyBindingResult(user, "user");

        validator.validate(user, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    public void shouldRejectDuplicateUser() {

        User existingUser = new User();

        User user = new User();
        user.setUsername("existinguser");
        user.setPassword("password123");
        user.setPasswordConfirm("password123");

        when(userService.findByUsername("existinguser"))
                .thenReturn(existingUser);

        Errors errors = new BeanPropertyBindingResult(user, "user");

        validator.validate(user, errors);

        assertTrue(errors.hasErrors());
    }

    @Test
    public void shouldRejectShortUsername() {

        User user = new User();
        user.setUsername("abc");
        user.setPassword("password123");
        user.setPasswordConfirm("password123");

        when(userService.findByUsername("abc"))
                .thenReturn(null);

        Errors errors = new BeanPropertyBindingResult(user, "user");

        validator.validate(user, errors);

        assertTrue(errors.hasErrors());
    }

    @Test
    public void shouldRejectShortPassword() {

        User user = new User();
        user.setUsername("validuser");
        user.setPassword("123");
        user.setPasswordConfirm("123");

        when(userService.findByUsername("validuser"))
                .thenReturn(null);

        Errors errors = new BeanPropertyBindingResult(user, "user");

        validator.validate(user, errors);

        assertTrue(errors.hasErrors());
    }

    @Test
    public void shouldRejectPasswordMismatch() {

        User user = new User();
        user.setUsername("validuser");
        user.setPassword("password123");
        user.setPasswordConfirm("differentPassword");

        when(userService.findByUsername("validuser"))
                .thenReturn(null);

        Errors errors = new BeanPropertyBindingResult(user, "user");

        validator.validate(user, errors);

        assertTrue(errors.hasErrors());
    }
}