package com.visualpathit.account.controllerTest;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import com.visualpathit.account.controller.FileUploadController;
import com.visualpathit.account.model.User;
import com.visualpathit.account.service.UserService;

public class FileUploadControllerTest {

    private FileUploadController controller;

    @Mock
    private UserService userService;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        controller = new FileUploadController();

        Field field = FileUploadController.class.getDeclaredField("userService");
        field.setAccessible(true);
        field.set(controller, userService);

        System.setProperty("catalina.home", System.getProperty("java.io.tmpdir"));
    }

    @Test
    public void shouldReturnErrorWhenFileEmpty() {

        MockMultipartFile file = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        String result = controller.uploadFileHandler(
                "image",
                "john",
                file);

        assertTrue(result.contains("empty"));
    }

    @Test
    public void shouldReturnUserNotFound() {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                "hello".getBytes());

        when(userService.findByUsername("john"))
                .thenReturn(null);

        String result = controller.uploadFileHandler(
                "image",
                "john",
                file);

        assertTrue(result.contains("User not found"));
    }

    @Test
    public void shouldUploadSuccessfully() {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                "hello".getBytes());

        User user = new User();

        when(userService.findByUsername("john"))
                .thenReturn(user);

        String result = controller.uploadFileHandler(
                "image",
                "john",
                file);

        assertTrue(result.contains("successfully"));
    }
}