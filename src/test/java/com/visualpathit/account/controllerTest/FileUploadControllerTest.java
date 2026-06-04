package com.visualpathit.account.controllerTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import com.visualpathit.account.controller.FileUploadController;
import com.visualpathit.account.model.User;
import com.visualpathit.account.service.UserService;

public class FileUploadControllerTest {

        @InjectMocks
        private FileUploadController controller;

        @Mock
        private UserService userService;

        @Mock
        private MultipartFile file;

        @Before
        public void setup() {
                MockitoAnnotations.initMocks(this);
        }

        @Test
        public void shouldRejectEmptyFile() {

                when(file.isEmpty()).thenReturn(true);

                String result = controller.uploadFileHandler("test", "john", file);

                assertTrue(result.contains("failed"));
        }

        @Test
        public void shouldReturnUserNotFound() {

                when(file.isEmpty()).thenReturn(false);

                when(userService.findByUsername("john")).thenReturn(null);

                String result = controller.uploadFileHandler("test", "john", file);

                assertTrue(result.contains("User not found"));
        }

        @Test
        public void shouldFailOnDirectoryCreation() throws Exception {

                when(file.isEmpty()).thenReturn(false);
                when(file.getBytes()).thenReturn("abc".getBytes());

                User user = new User();
                when(userService.findByUsername("john")).thenReturn(user);

                // simulate bad path
                System.setProperty("catalina.home", "");

                String result = controller.uploadFileHandler("test", "john", file);

                assertNotNull(result);
        }
}