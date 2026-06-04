package com.visualpathit.account.controllerTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.visualpathit.account.controller.UserController;
import com.visualpathit.account.model.User;
import com.visualpathit.account.service.*;

public class UserControllerTest {

	@InjectMocks
	private UserController controller;

	@Mock
	private UserService userService;
	@Mock
	private SecurityService securityService;
	@Mock
	private ProducerService producerService;
	@Mock
	private com.visualpathit.account.validator.UserValidator userValidator;
	@Mock
	private Model model;
	@Mock
	private BindingResult bindingResult;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldShowRegistrationPage() {
		String view = controller.registration(model);
		assertEquals("registration", view);
	}

	@Test
	public void shouldFailRegistrationWhenErrors() {

		User user = new User();

		doAnswer(invocation -> {
			BindingResult br = invocation.getArgument(1);
			br.reject("error");
			return null;
		}).when(userValidator).validate(any(), any());

		when(bindingResult.hasErrors()).thenReturn(true);

		String view = controller.registration(user, bindingResult, model);

		assertEquals("registration", view);
	}

	@Test
	public void shouldRegisterSuccessfully() {

		User user = new User();
		user.setUsername("john");
		user.setPasswordConfirm("pass");

		when(bindingResult.hasErrors()).thenReturn(false);
		when(securityService.autologin(any(), any())).thenReturn(true);

		String view = controller.registration(user, bindingResult, model);

		assertEquals("redirect:/welcome", view);
	}

	@Test
	public void shouldFailLogin() {

		User user = new User();
		user.setUsername("john");
		user.setPassword("pass");

		when(securityService.autologin(any(), any())).thenReturn(false);

		String view = controller.loginPost(user, model);

		assertEquals("login", view);
	}

	@Test
	public void shouldReturnUsersList() {

		when(userService.getList()).thenReturn(Arrays.asList(new User(), new User()));

		String view = controller.getAllUsers(model);

		assertEquals("userList", view);
	}

	@Test
	public void shouldReturnWelcome() {
		assertEquals("welcome", controller.welcome(model));
	}
}