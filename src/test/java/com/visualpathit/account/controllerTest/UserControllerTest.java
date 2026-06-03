package com.visualpathit.account.controllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.visualpathit.account.controller.UserController;
import com.visualpathit.account.model.User;
import com.visualpathit.account.service.ProducerService;
import com.visualpathit.account.service.SecurityService;
import com.visualpathit.account.service.UserService;
import com.visualpathit.account.setup.StandaloneMvcTestViewResolver;
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

	// ❌ LOGIN FAIL FLOW
	@Test
	public void shouldFailLoginPost() throws Exception {

		when(securityService.autologin(anyString(), anyString()))
				.thenReturn(false);

		mockMvc.perform(post("/login")
				.param("username", "john")
				.param("password", "123"))
				.andExpect(status().isOk())
				.andExpect(view().name("login"));
	}

	// ✅ LOGIN SUCCESS FLOW (FIXED FOR REDIRECT)
	@Test
	public void shouldSucceedLoginPost() throws Exception {

		when(securityService.autologin(anyString(), anyString()))
				.thenReturn(true);

		mockMvc.perform(post("/login")
				.param("username", "john")
				.param("password", "123"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/welcome"));
	}
}