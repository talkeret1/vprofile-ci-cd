package com.visualpathit.account.validatorTest;

import com.visualpathit.account.model.User;
import com.visualpathit.account.service.UserService;
import com.visualpathit.account.validator.UserValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.lang.reflect.Field;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserValidatorTest {

    private UserValidator validator;

    @Mock
    private UserService userService;

    @Before
    public void setup() throws Exception {

        validator = new UserValidator();

        // Inject mock manually (same approach you already use)
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
        user.setUsername("validuser123");
        user.setPassword("password123");
        user.setPasswordConfirm("password123");

        when(userService.findByUsername("validuser123"))
                .thenReturn(null);

        Errors errors = new BeanPropertyBindingResult(user, "user");

        validator.validate(user, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    public void shouldRejectDuplicateUser() {

        User existing = new User();

        User user = new User();
        user.setUsername("existinguser");
        user.setPassword("password123");
        user.setPasswordConfirm("password123");

        when(userService.findByUsername("existinguser"))
                .thenReturn(existing);

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
    public void shouldRejectLongUsername() {

        User user = new User();
        user.setUsername("a".repeat(40));
        user.setPassword("password123");
        user.setPasswordConfirm("password123");

        when(userService.findByUsername(user.getUsername()))
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
    public void shouldRejectLongPassword() {

        User user = new User();
        user.setUsername("validuser");
        user.setPassword("a".repeat(40));
        user.setPasswordConfirm("a".repeat(40));

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
        user.setPasswordConfirm("different");

        when(userService.findByUsername("validuser"))
                .thenReturn(null);

        Errors errors = new BeanPropertyBindingResult(user, "user");

        validator.validate(user, errors);

        assertTrue(errors.hasErrors());
    }

    @Test
    public void shouldRejectNullUser() {

        User user = new User();

        Errors errors = new BeanPropertyBindingResult(user, "user");

        validator.validate(user, errors);

        assertTrue(errors.hasErrors());
    }

    @Test
    public void shouldRejectEmptyUsername() {

        User user = new User();
        user.setUsername("");
        user.setPassword("password123");
        user.setPasswordConfirm("password123");

        when(userService.findByUsername(""))
                .thenReturn(null);

        Errors errors = new BeanPropertyBindingResult(user, "user");

        validator.validate(user, errors);

        assertTrue(errors.hasErrors());
    }
}