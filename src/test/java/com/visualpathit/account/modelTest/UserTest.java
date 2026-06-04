package com.visualpathit.account.modelTest;

import com.visualpathit.account.model.Role;
import com.visualpathit.account.model.User;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void shouldFullyPopulateUserFields() {

        // arrange
        Role role = new Role();
        role.setId(1L);
        role.setName("Admin");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();

        // act - set ALL fields
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("secret");
        user.setUserEmail("test@example.com");
        user.setPasswordConfirm("secretConfirm");
        user.setProfileImg("img.png");
        user.setProfileImgPath("/images/img.png");

        user.setDateOfBirth("1990-01-01");
        user.setFatherName("father");
        user.setMotherName("mother");
        user.setGender("male");
        user.setMaritalStatus("single");
        user.setPermanentAddress("permanent");
        user.setTempAddress("temp");
        user.setPrimaryOccupation("developer");
        user.setSecondaryOccupation("trainer");
        user.setSkills("java,spring");
        user.setPhoneNumber("123456");
        user.setSecondaryPhoneNumber("654321");
        user.setNationality("IL");
        user.setLanguage("Hebrew");
        user.setWorkingExperience("5 years");

        user.setRoles(roles);

        // assert - cover ALL getters
        assertEquals(Long.valueOf(1L), user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("secret", user.getPassword());
        assertEquals("test@example.com", user.getUserEmail());
        assertEquals("secretConfirm", user.getPasswordConfirm());
        assertEquals("img.png", user.getProfileImg());
        assertEquals("/images/img.png", user.getProfileImgPath());

        assertEquals("1990-01-01", user.getDateOfBirth());
        assertEquals("father", user.getFatherName());
        assertEquals("mother", user.getMotherName());
        assertEquals("male", user.getGender());
        assertEquals("single", user.getMaritalStatus());
        assertEquals("permanent", user.getPermanentAddress());
        assertEquals("temp", user.getTempAddress());
        assertEquals("developer", user.getPrimaryOccupation());
        assertEquals("trainer", user.getSecondaryOccupation());
        assertEquals("java,spring", user.getSkills());
        assertEquals("123456", user.getPhoneNumber());
        assertEquals("654321", user.getSecondaryPhoneNumber());
        assertEquals("IL", user.getNationality());
        assertEquals("Hebrew", user.getLanguage());
        assertEquals("5 years", user.getWorkingExperience());

        assertNotNull(user.getRoles());
        assertEquals(1, user.getRoles().size());
    }
}