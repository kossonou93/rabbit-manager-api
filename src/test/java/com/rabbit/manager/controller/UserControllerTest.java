package com.rabbit.manager.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.rabbit.manager.dto.RoleDTO;
import com.rabbit.manager.dto.UserDTO;
import com.rabbit.manager.security.JwtUtils;
import com.rabbit.manager.service.RoleService;
import com.rabbit.manager.service.UserService;
import com.rabbit.manager.utility.ApiResponse;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;
    
    @Mock
    private RoleService roleService;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtils jwtUtils;

    @Test
    public void testGetAllUsers_Success() {
        // Préparer les données de test
        List<UserDTO> users = new ArrayList<>();
        RoleDTO roleDTO1 = roleService.getByName("USER");
        if(roleDTO1.equals(null)) {
        	
        }
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        roles.add("ADMIN");
        users.add(new UserDTO("testId1", "user1", "user1", "user1@example.com", "password", true, roles));
        users.add(new UserDTO("testId2", "user2", "user2", "user2@example.com", "password2", true, roles));

        when(userService.allUser()).thenReturn(users);

        // Exécuter la méthode à tester
        ApiResponse<List<UserDTO>> response = userController.getAllUsers();

        // Vérifier les résultats
        assertTrue(response.isSuccess());
        assertEquals("Users found successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(2, response.getData().size());
        assertEquals("user1", response.getData().get(0).getUsername());
        assertEquals("user2@example.com", response.getData().get(1).getEmail());
    }

    @Test
    public void testGetAllUsers_Failure() {
        // Préparer les données de test
        when(userService.allUser()).thenThrow(new RuntimeException("Database error"));

        // Exécuter la méthode à tester
        ApiResponse<List<UserDTO>> response = userController.getAllUsers();

        // Vérifier les résultats
        assertFalse(response.isSuccess());
        assertEquals("Failed to get all users", response.getMessage());
        assertNull(response.getData());
    }

}
