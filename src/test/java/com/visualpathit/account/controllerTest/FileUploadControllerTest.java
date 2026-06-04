package com.visualpathit.account.controllerTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

                // חשוב: מונע בעיות של path ב-Jenkins / מקומית
                System.setProperty("catalina.home", System.getProperty("java.io.tmpdir"));
        }

        // -------------------------
        // EMPTY FILE PATH
        // -------------------------

        @Test
        public void shouldRejectEmptyFile() {

                when(file.isEmpty()).thenReturn(true);

                String result = controller.uploadFileHandler("test", "john", file);

                assertTrue(result.contains("failed"));
        }

        // -------------------------
        // USER NOT FOUND
        // -------------------------

        @Test
        public void shouldReturnUserNotFound() {

                when(file.isEmpty()).thenReturn(false);

                when(userService.findByUsername("john")).thenReturn(null);

                String result = controller.uploadFileHandler("test", "john", file);

                assertTrue(result.contains("User not found"));
        }

        // -------------------------
        // SUCCESS PATH
        // -------------------------

        @Test
        public void shouldUploadFileSuccessfully() throws Exception {

                when(file.isEmpty()).thenReturn(false);
                when(file.getBytes()).thenReturn("abc".getBytes());

                User user = new User();
                when(userService.findByUsername("john")).thenReturn(user);

                String result = controller.uploadFileHandler("test", "john", file);

                assertTrue(result.contains("successfully"));
                verify(userService, times(1)).save(any(User.class));
        }

        // -------------------------
        // EXCEPTION PATH
        // -------------------------

        @Test
        public void shouldHandleExceptionGracefully() {

                when(file.isEmpty()).thenReturn(false);
                when(userService.findByUsername("john")).thenThrow(new RuntimeException("fail"));

                String result = controller.uploadFileHandler("test", "john", file);

                assertTrue(result.contains("failed"));
        }
}