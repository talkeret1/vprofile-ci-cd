package com.visualpathit.account.controllerTest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.visualpathit.account.controller.UserController;
import com.visualpathit.account.model.User;
import com.visualpathit.account.service.ProducerService;
import com.visualpathit.account.service.SecurityService;
import com.visualpathit.account.service.UserService;
import com.visualpathit.account.setup.StandaloneMvcTestViewResolver;
import com.visualpathit.account.utils.MemcachedUtils;
import com.visualpathit.account.validator.UserValidator;

public class UserControllerTest {

	@Mock
	private UserService userService;

	@Mock
	private SecurityService securityService;

	@Mock
	private UserValidator userValidator;

	@Mock
	private ProducerService producerService;

	@InjectMocks
	private UserController controller;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		mockMvc = MockMvcBuilders
				.standaloneSetup(controller)
				.setViewResolvers(new StandaloneMvcTestViewResolver())
				.build();
	}

	@Test
	public void shouldLoadRegistrationPage() throws Exception {
		mockMvc.perform(get("/registration"))
				.andExpect(status().isOk())
				.andExpect(view().name("registration"));
	}

	@Test
	public void shouldLoadLoginPage() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("login"));
	}

	@Test
	public void shouldLoadWelcomePage() throws Exception {
		mockMvc.perform(get("/welcome"))
				.andExpect(status().isOk())
				.andExpect(view().name("welcome"));
	}

	@Test
	public void shouldLoadIndexHomePage() throws Exception {
		mockMvc.perform(get("/index"))
				.andExpect(status().isOk())
				.andExpect(view().name("index_home"));
	}

	@Test
	public void shouldLoadUsersPage() throws Exception {
		when(userService.getList()).thenReturn(List.of(new User()));

		mockMvc.perform(get("/users"))
				.andExpect(status().isOk())
				.andExpect(view().name("userList"));
	}

	@Test
	public void shouldFailLoginPost() throws Exception {
		when(securityService.autologin(anyString(), anyString())).thenReturn(false);

		mockMvc.perform(post("/login")
				.param("username", "john")
				.param("password", "123"))
				.andExpect(status().isOk())
				.andExpect(view().name("login"));
	}

	@Test
	public void shouldSucceedLoginPost() throws Exception {
		when(securityService.autologin(anyString(), anyString())).thenReturn(true);

		mockMvc.perform(post("/login")
				.param("username", "john")
				.param("password", "123"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/welcome"));
	}

	@Test
	public void shouldRegisterUserSuccessfully() throws Exception {
		when(securityService.autologin(anyString(), anyString())).thenReturn(true);

		mockMvc.perform(post("/registration")
				.param("username", "testuser")
				.param("password", "password123")
				.param("passwordConfirm", "password123"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/welcome"));
	}

	@Test
	public void shouldRedirectToLoginWhenAutologinFails() throws Exception {
		when(securityService.autologin(anyString(), anyString())).thenReturn(false);

		mockMvc.perform(post("/registration")
				.param("username", "testuser")
				.param("password", "password123")
				.param("passwordConfirm", "password123"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login?error"));
	}

	@Test
	public void shouldLoadUserUpdatePage() throws Exception {
		User user = new User();
		user.setUsername("john");

		when(userService.findByUsername("john")).thenReturn(user);

		mockMvc.perform(get("/user/john"))
				.andExpect(status().isOk())
				.andExpect(view().name("userUpdate"));
	}

	@Test
	public void shouldUpdateUserProfile() throws Exception {
		User existingUser = new User();
		existingUser.setUsername("john");

		when(userService.findByUsername("john")).thenReturn(existingUser);

		mockMvc.perform(post("/user/john")
				.param("username", "johnUpdated")
				.param("userEmail", "john@test.com"))
				.andExpect(status().isOk())
				.andExpect(view().name("welcome"));
	}

	@Test
	public void shouldReturnUserFromCache() throws Exception {
		User cachedUser = new User();
		cachedUser.setUsername("cachedUser");

		try (MockedStatic<MemcachedUtils> mocked = Mockito.mockStatic(MemcachedUtils.class)) {

			mocked.when(() -> MemcachedUtils.memcachedGetData("1"))
					.thenReturn(cachedUser);

			mockMvc.perform(get("/users/1"))
					.andExpect(status().isOk())
					.andExpect(view().name("user"))
					.andExpect(model().attribute("user", cachedUser))
					.andExpect(model().attributeExists("Result"));
		}
	}

	@Test
	public void shouldReturnUserFromDatabaseWhenCacheEmpty() throws Exception {
		User dbUser = new User();
		dbUser.setUsername("dbUser");

		try (MockedStatic<MemcachedUtils> mocked = Mockito.mockStatic(MemcachedUtils.class)) {

			mocked.when(() -> MemcachedUtils.memcachedGetData("2"))
					.thenReturn(null);

			mocked.when(() -> MemcachedUtils.memcachedSetData(dbUser, "2"))
					.thenReturn("Memcached OK");

			when(userService.findById(2L)).thenReturn(dbUser);

			mockMvc.perform(get("/users/2"))
					.andExpect(status().isOk())
					.andExpect(view().name("user"))
					.andExpect(model().attribute("user", dbUser))
					.andExpect(model().attribute("Result", "Memcached OK"));
		}
	}

	@Test
	public void shouldHandleExceptionInGetOneUser() throws Exception {
		try (MockedStatic<MemcachedUtils> mocked = Mockito.mockStatic(MemcachedUtils.class)) {

			mocked.when(() -> MemcachedUtils.memcachedGetData("bad"))
					.thenThrow(new RuntimeException("fail"));

			mockMvc.perform(get("/users/bad"))
					.andExpect(status().isOk())
					.andExpect(view().name("user"));
		}
	}
}