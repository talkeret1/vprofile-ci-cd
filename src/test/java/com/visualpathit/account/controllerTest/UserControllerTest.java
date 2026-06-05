package com.visualpathit.account.controllerTest;

import com.visualpathit.account.controller.UserController;
import com.visualpathit.account.model.User;
import com.visualpathit.account.service.SecurityService;
import com.visualpathit.account.service.UserService;
import com.visualpathit.account.utils.MemcachedUtils;
import com.visualpathit.account.validator.UserValidator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

	@InjectMocks
	private UserController controller;

	@Mock
	private UserService userService;

	@Mock
	private SecurityService securityService;

	@Mock
	private UserValidator userValidator;

	@Mock
	private Model model;

	@Mock
	private BindingResult bindingResult;

	@Before
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	// -------------------------
	// REGISTRATION FLOW
	// -------------------------

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
		}).when(userValidator).validate(any(User.class), any(BindingResult.class));

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
		when(securityService.autologin(anyString(), anyString())).thenReturn(true);

		String view = controller.registration(user, bindingResult, model);

		assertEquals("redirect:/welcome", view);
	}

	// -------------------------
	// LOGIN FLOW
	// -------------------------

	@Test
	public void shouldFailLogin() {

		User user = new User();
		user.setUsername("john");
		user.setPassword("pass");

		when(securityService.autologin(anyString(), anyString())).thenReturn(false);

		String view = controller.loginPost(user, model);

		assertEquals("login", view);
	}

	@Test
	public void shouldSuccessLogin() {

		User user = new User();
		user.setUsername("john");
		user.setPassword("pass");

		when(securityService.autologin(anyString(), anyString())).thenReturn(true);

		String view = controller.loginPost(user, model);

		assertEquals("redirect:/welcome", view);
	}

	// -------------------------
	// USERS LIST
	// -------------------------

	@Test
	public void shouldReturnUsersList() {

		when(userService.getList()).thenReturn(Arrays.asList(new User(), new User()));

		String view = controller.getAllUsers(model);

		assertEquals("userList", view);
	}

	// -------------------------
	// CACHE TESTS
	// -------------------------

	@Test
	public void shouldReturnUserFromCache() {

		User cachedUser = new User();
		cachedUser.setId(1L);

		try (MockedStatic<MemcachedUtils> mocked = mockStatic(MemcachedUtils.class)) {

			mocked.when(() -> MemcachedUtils.memcachedGetData("1"))
					.thenReturn(cachedUser);

			String view = controller.getOneUser("1", model);

			assertEquals("user", view);
			verify(model).addAttribute(eq("user"), any());
		}
	}

	@Test
	public void shouldReturnUserFromDbWhenCacheMiss() {

		User dbUser = new User();
		dbUser.setId(2L);

		try (MockedStatic<MemcachedUtils> mocked = mockStatic(MemcachedUtils.class)) {

			mocked.when(() -> MemcachedUtils.memcachedGetData("2"))
					.thenReturn(null);

			mocked.when(() -> MemcachedUtils.memcachedSetData(any(User.class), eq("2")))
					.thenReturn("OK");

			when(userService.findById(2L)).thenReturn(dbUser);

			String view = controller.getOneUser("2", model);

			assertEquals("user", view);
			verify(model).addAttribute(eq("user"), eq(dbUser));
		}
	}

	@Test
	public void shouldHandleExceptionGracefully() {

		try (MockedStatic<MemcachedUtils> mocked = mockStatic(MemcachedUtils.class)) {

			mocked.when(() -> MemcachedUtils.memcachedGetData("bad"))
					.thenThrow(new RuntimeException("fail"));

			String view = controller.getOneUser("bad", model);

			assertEquals("user", view);
			verify(model).addAttribute(eq("Result"), any());
		}
	}

	// -------------------------
	// UPDATE FLOW
	// -------------------------

	@Test
	public void shouldUpdateUserProfile() {

		User existing = new User();
		existing.setUsername("old");

		User updated = new User();
		updated.setUsername("new");

		when(userService.findByUsername("old")).thenReturn(existing);

		String view = controller.userUpdateProfile("old", updated);

		assertEquals("welcome", view);
		verify(userService).save(existing);
	}

	// -------------------------
	// SIMPLE PAGES (FIXED)
	// -------------------------

	@Test
	public void shouldReturnWelcome() {
		assertEquals("welcome", controller.welcome());
	}

	@Test
	public void shouldReturnIndex() {
		assertEquals("index_home", controller.indexHome());
	}

	@Test
	public void shouldReturnLoginPage() {
		String view = controller.login(model, null, null);
		assertEquals("login", view);
	}
}