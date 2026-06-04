package com.visualpathit.account.serviceTest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.visualpathit.account.model.Role;
import com.visualpathit.account.model.User;
import com.visualpathit.account.repository.RoleRepository;
import com.visualpathit.account.repository.UserRepository;
import com.visualpathit.account.service.UserServiceImpl;

public class UserServiceImplTest {

    private UserServiceImpl service;
    private AutoCloseable closeable;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Before
    public void setup() throws Exception {

        closeable = MockitoAnnotations.openMocks(this);

        service = new UserServiceImpl();

        inject("userRepository", userRepository);
        inject("roleRepository", roleRepository);
        inject("bCryptPasswordEncoder", encoder);
    }

    @After
    public void tearDown() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
    }

    private void inject(String fieldName, Object value) throws Exception {
        Field field = UserServiceImpl.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(service, value);
    }

    @Test
    public void shouldSaveUser() {

        User user = new User();
        user.setPassword("secret");

        Role role = new Role();
        role.setName("ADMIN");

        when(encoder.encode("secret")).thenReturn("encoded");
        when(roleRepository.findAll()).thenReturn(Arrays.asList(role));

        service.save(user);

        assertEquals("encoded", user.getPassword());

        verify(userRepository).save(user);
    }

    @Test
    public void shouldFindByUsername() {

        User user = new User();

        when(userRepository.findByUsername("john"))
                .thenReturn(user);

        assertEquals(user, service.findByUsername("john"));
    }

    @Test
    public void shouldFindAllUsers() {

        List<User> users = Arrays.asList(new User(), new User());

        when(userRepository.findAll()).thenReturn(users);

        assertEquals(2, service.getList().size());
    }

    @Test
    public void shouldFindById() {

        User user = new User();

        when(userRepository.findById(1L))
                .thenReturn(user);

        assertEquals(user, service.findById(1L));
    }
}