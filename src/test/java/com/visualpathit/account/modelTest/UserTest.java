package com.visualpathit.account.modelTest;

import com.visualpathit.account.model.Role;
import com.visualpathit.account.model.User;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class UserTest {

    @Test
    public void shouldCreateUserWithRoles() {

        // arrange
        Role role = new Role();
        role.setId(1L);
        role.setName("Admin");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("secret");
        user.setUserEmail("test@example.com");
        user.setRoles(roles);

        // assert
        assertEquals(Long.valueOf(1L), user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("secret", user.getPassword());
        assertEquals("test@example.com", user.getUserEmail());
        assertEquals(1, user.getRoles().size());
    }
}