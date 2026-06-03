package com.visualpathit.account.serviceTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.visualpathit.account.model.Role;
import com.visualpathit.account.model.User;
import com.visualpathit.account.repository.UserRepository;
import com.visualpathit.account.service.UserDetailsServiceImpl;

public class UserDetailsServiceImplTest {

    private UserDetailsServiceImpl service;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setup() throws Exception {

        MockitoAnnotations.initMocks(this);

        service = new UserDetailsServiceImpl();

        Field field = UserDetailsServiceImpl.class.getDeclaredField("userRepository");

        field.setAccessible(true);
        field.set(service, userRepository);
    }

    @Test
    public void shouldLoadUser() {

        Role role = new Role();
        role.setName("ADMIN");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setUsername("john");
        user.setPassword("secret");
        user.setRoles(roles);

        when(userRepository.findByUsername("john"))
                .thenReturn(user);

        org.springframework.security.core.userdetails.UserDetails result = service.loadUserByUsername("john");

        assertEquals("john", result.getUsername());

        assertTrue(
                result.getAuthorities()
                        .stream()
                        .anyMatch(a -> a.getAuthority().equals("ADMIN")));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowWhenUserMissing() {

        when(userRepository.findByUsername("missing"))
                .thenReturn(null);

        service.loadUserByUsername("missing");
    }
}