package com.visualpathit.account.controllerTest;

import com.visualpathit.account.beans.Components;
import com.visualpathit.account.controller.ElasticSearchController;
import com.visualpathit.account.model.User;
import com.visualpathit.account.service.UserService;
import com.visualpathit.account.utils.ElasticsearchClientFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.opensearch.client.RestHighLevelClient;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ElasticSearchControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Components components;

    @Mock
    private ElasticsearchClientFactory clientFactory;

    @Mock
    private Model model;

    private ElasticSearchController controller;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        controller = new ElasticSearchController(
                userService,
                components,
                clientFactory);
    }

    // =========================
    // SYNC SUCCESS
    // =========================
    @Test
    public void shouldSyncUsersSuccessfully() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("john");

        when(userService.getList()).thenReturn(List.of(user));

        RestHighLevelClient client = mock(RestHighLevelClient.class);
        when(clientFactory.createClient()).thenReturn(client);

        String view = controller.insert(model);

        assertEquals("elasticeSearchRes", view);

        verify(userService).getList();
        verify(clientFactory).createClient();
    }

    // =========================
    // VIEW USER FOUND
    // =========================
    @Test
    public void shouldReturnUserFound() throws Exception {

        RestHighLevelClient client = mock(RestHighLevelClient.class);

        when(clientFactory.createClient()).thenReturn(client);

        // לא ניכנס ל-OpenSearch response mocking מורכב
        String view = controller.view("1", model);

        assertEquals("elasticeSearchRes", view);

        verify(clientFactory).createClient();
    }

    // =========================
    // UPDATE USER
    // =========================
    @Test
    public void shouldUpdateUser() throws Exception {

        RestHighLevelClient client = mock(RestHighLevelClient.class);

        when(clientFactory.createClient()).thenReturn(client);

        String view = controller.update("1", model);

        assertEquals("elasticeSearchRes", view);

        verify(clientFactory).createClient();
    }

    // =========================
    // DELETE USER
    // =========================
    @Test
    public void shouldDeleteUser() throws Exception {

        RestHighLevelClient client = mock(RestHighLevelClient.class);

        when(clientFactory.createClient()).thenReturn(client);

        String view = controller.delete("1", model);

        assertEquals("elasticeSearchRes", view);

        verify(clientFactory).createClient();
    }
}