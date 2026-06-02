package com.visualpathit.account.modelTest;

import com.visualpathit.account.model.Role;
import com.visualpathit.account.model.User;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class RoleTest {

    @Test
    public void shouldCreateRoleWithUsers() {

        // arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("password");
        user.setUserEmail("test@example.com");

        Set<User> users = new HashSet<>();
        users.add(user);

        Role role = new Role();
        role.setId(1L);
        role.setName("Admin");
        role.setUsers(users);

        // assert
        assertEquals(Long.valueOf(1L), role.getId());
        assertEquals("Admin", role.getName());
        assertEquals(1, role.getUsers().size());
    }
}