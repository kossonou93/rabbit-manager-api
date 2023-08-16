package com.rabbit.manager.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.rabbit.manager.dto.RoleDTO;
import com.rabbit.manager.service.RoleService;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class RoleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
    }

    @Test
    public void testGetAllRoles_Success() throws Exception {
        // Préparer les données de test
        List<RoleDTO> roles = new ArrayList<>();
        roles.add(new RoleDTO("1", "ADMIN"));
        roles.add(new RoleDTO("2", "USER"));

        when(roleService.allRole()).thenReturn(roles);

        // Effectuer la requête HTTP simulée
        mockMvc.perform(get("/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Roles found successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("ADMIN"))
                .andExpect(jsonPath("$.data[1].name").value("USER"));

        // Vérifier que roleService.allRole() a été appelé
        verify(roleService, times(1)).allRole();
    }

    @Test
    public void testGetAllRoles_Failure() throws Exception {
        // Le service de rôle renvoie une exception lorsque allRole() est appelé
        when(roleService.allRole()).thenThrow(new RuntimeException("Database error"));

        // Effectuer la requête HTTP simulée
        mockMvc.perform(get("/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Failed to get all roles"))
                .andExpect(jsonPath("$.data").doesNotExist());

        // Vérifier que roleService.allRole() a été appelé
        verify(roleService, times(1)).allRole();
    }

}
