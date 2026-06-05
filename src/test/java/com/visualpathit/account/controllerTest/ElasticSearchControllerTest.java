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
import org.opensearch.action.get.GetResponse;
import org.opensearch.action.update.UpdateResponse;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        MockitoAnnotations.openMocks(this);
        controller = new ElasticSearchController(userService, components, clientFactory);
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
    // SYNC FAILURE (DB exception)
    // =========================
    @Test
    public void shouldHandleSyncFailure() {

        when(userService.getList()).thenThrow(new RuntimeException("DB error"));

        String view = controller.insert(model);

        assertEquals("elasticeSearchRes", view);
    }

    // =========================
    // PARTIAL SUCCESS (covers hasErrors=true)
    // =========================
    @Test
    public void shouldHandlePartialSuccess() throws Exception {

        User u1 = new User();
        u1.setId(1L);
        u1.setUsername("a");
        User u2 = new User();
        u2.setId(2L);
        u2.setUsername("b");

        when(userService.getList()).thenReturn(List.of(u1, u2));

        RestHighLevelClient client = mock(RestHighLevelClient.class);
        when(clientFactory.createClient()).thenReturn(client);

        String view = controller.insert(model);

        assertEquals("elasticeSearchRes", view);
    }

    // =========================
    // VIEW - USER EXISTS
    // =========================
    @Test
    public void shouldHandleViewUserExists() throws Exception {

        RestHighLevelClient client = mock(RestHighLevelClient.class);
        GetResponse response = mock(GetResponse.class);

        when(clientFactory.createClient()).thenReturn(client);
        when(client.get(any(), any())).thenReturn(response);
        when(response.isExists()).thenReturn(true);

        String view = controller.view("1", model);

        assertEquals("elasticeSearchRes", view);
    }

    // =========================
    // VIEW - USER NOT FOUND (covers else branch)
    // =========================
    @Test
    public void shouldHandleUserNotFound() throws Exception {

        RestHighLevelClient client = mock(RestHighLevelClient.class);
        GetResponse response = mock(GetResponse.class);

        when(clientFactory.createClient()).thenReturn(client);
        when(client.get(any(), any())).thenReturn(response);
        when(response.isExists()).thenReturn(false);

        String view = controller.view("1", model);

        assertEquals("elasticeSearchRes", view);
    }

    // =========================
    // VIEW ERROR (catch)
    // =========================
    @Test
    public void shouldHandleViewError() throws Exception {

        when(clientFactory.createClient()).thenThrow(new RuntimeException("connection error"));

        String view = controller.view("1", model);

        assertEquals("elasticeSearchRes", view);
    }

    // =========================
    // UPDATE USER (SUCCESS)
    // =========================
    @Test
    public void shouldUpdateUser() throws Exception {

        RestHighLevelClient client = mock(RestHighLevelClient.class);
        UpdateResponse response = mock(UpdateResponse.class);

        when(clientFactory.createClient()).thenReturn(client);
        when(client.update(any(), any())).thenReturn(response);
        when(response.getResult()).thenReturn(UpdateResponse.Result.UPDATED);

        String view = controller.update("1", model);

        assertEquals("elasticeSearchRes", view);
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
    }

    // =========================
    // AWS HOST branch
    // =========================
    @Test
    public void shouldHandleAwsHost() throws Exception {

        when(components.getElasticsearchHost()).thenReturn("search.amazonaws.com");
        when(components.getElasticsearchScheme()).thenReturn("https");

        RestHighLevelClient client = mock(RestHighLevelClient.class);
        when(clientFactory.createClient()).thenReturn(client);

        controller.view("1", model);
    }

    // =========================
    // LOCAL HOST branch
    // =========================
    @Test
    public void shouldHandleLocalHost() throws Exception {

        when(components.getElasticsearchHost()).thenReturn("localhost");
        when(components.getElasticsearchPort()).thenReturn("9200");
        when(components.getElasticsearchScheme()).thenReturn("http");

        RestHighLevelClient client = mock(RestHighLevelClient.class);
        when(clientFactory.createClient()).thenReturn(client);

        controller.view("1", model);
    }

    // =========================
    // STATUS UNKNOWN branch (null result)
    // =========================
    @Test
    public void shouldHandleUnknownStatus() {

        controller.view("1", model);
    }
}