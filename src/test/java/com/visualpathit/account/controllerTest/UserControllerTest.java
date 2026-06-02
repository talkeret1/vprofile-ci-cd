package com.visualpathit.account.controllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.visualpathit.account.controller.UserController;
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
				.andExpect(view().name("registration"))
				.andExpect(forwardedUrl("registration"));
	}

	@Test
	public void shouldLoadLoginPage() throws Exception {

		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("login"))
				.andExpect(forwardedUrl("login"));
	}

	@Test
	public void shouldLoadWelcomePage() throws Exception {

		mockMvc.perform(get("/welcome"))
				.andExpect(status().isOk())
				.andExpect(view().name("welcome"))
				.andExpect(forwardedUrl("welcome"));
	}

	@Test
	public void shouldLoadRootAsLogin() throws Exception {

		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(view().name("login"))
				.andExpect(forwardedUrl("login"));
	}

	@Test
	public void shouldLoadIndexHomePage() throws Exception {

		mockMvc.perform(get("/index"))
				.andExpect(status().isOk())
				.andExpect(view().name("index_home"))
				.andExpect(forwardedUrl("index_home"));
	}
}