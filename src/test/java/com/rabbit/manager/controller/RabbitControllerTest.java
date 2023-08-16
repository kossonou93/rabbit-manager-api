package com.rabbit.manager.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.rabbit.manager.dto.RabbitDTO;
import com.rabbit.manager.model.Rabbit;
import com.rabbit.manager.repository.RabbitRepository;
import com.rabbit.manager.service.RabbitService;
import com.rabbit.manager.utility.ApiResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class RabbitControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RabbitService rabbitService;

    @InjectMocks
    private RabbitController rabbitController;
    
    @InjectMocks
    private RabbitRepository rabbitRepository;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(rabbitController).build();
    }
    
    // Test pour vérifier la récupération des Lapins
    @Test
    public void testGetAllRabbits_Success() throws Exception {
        // Préparer les données de test
        List<RabbitDTO> rabbits = new ArrayList<>();
        rabbits.add(new RabbitDTO("1", "Rabbit1", true, "image1.jpg"));
        rabbits.add(new RabbitDTO("2", "Rabbit2", true, "image2.jpg"));

        when(rabbitService.allRabbit()).thenReturn(rabbits);

        // Effectuer la requête HTTP simulée
        mockMvc.perform(get("/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Rabbits found successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("Rabbit1"))
                .andExpect(jsonPath("$.data[0].imagePath").value("image1.jpg"))
                .andExpect(jsonPath("$.data[0].status").value(true))
                .andExpect(jsonPath("$.data[1].name").value("Rabbit2"))
                .andExpect(jsonPath("$.data[1].imagePath").value("image2.jpg"))
                .andExpect(jsonPath("$.data[1].status").value(true));

        // Vérifier que rabbitService.allRabbit() a été appelé
        verify(rabbitService, times(1)).allRabbit();
    }

    @Test
    public void testGetAllRabbits_Failure() throws Exception {
        // Le service de lapin renvoie une exception lorsque allRabbit() est appelé
        when(rabbitService.allRabbit()).thenThrow(new RuntimeException("Database error"));

        // Effectuer la requête HTTP simulée
        mockMvc.perform(get("/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Failed to get all rabbits"))
                .andExpect(jsonPath("$.data").doesNotExist());

        // Vérifier que rabbitService.allRabbit() a été appelé
        verify(rabbitService, times(1)).allRabbit();
    }
    
    // Test pour vérifier la récupération d'un Lapin par son Id
    @Test
    public void testGetRabbitById_Success() throws Exception {
        // Préparer les données de test
        List<RabbitDTO> rabbits = new ArrayList<>();
        rabbits.add(new RabbitDTO("1", "Rabbit1", true, "image1.jpg"));
        rabbits.add(new RabbitDTO("2", "Rabbit2", true, "image2.jpg"));

        when(rabbitService.getById(rabbits.get(0).getId())).thenReturn(rabbits.get(0));

        // Effectuer la requête HTTP simulée
        mockMvc.perform(get("/{id}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Rabbit found successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("Rabbit1"))
                .andExpect(jsonPath("$.data[0].imagePath").value("image1.jpg"))
                .andExpect(jsonPath("$.data[0].status").value(true));

        // Vérifier que rabbitService.getById() a été appelé
        verify(rabbitService, times(1)).getById("1");
    }
    
    @Test
    public void testGetRabbitById_Failure() throws Exception {
        // Le service de lapin renvoie une exception lorsque getById() est appelé
        when(rabbitService.getById("1")).thenThrow(new RuntimeException("Database error"));

        // Effectuer la requête HTTP simulée
        mockMvc.perform(get("/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Failed to get by rabbit id"))
                .andExpect(jsonPath("$.data").doesNotExist());

        // Vérifier que rabbitService.getById() a été appelé
        verify(rabbitService, times(1)).getById("1");
    }
    
    // Test pour vérifier l'ajouter d'un Lapin
    @Test
    public void testAddRabbitSuccess() {
        RabbitDTO rabbitDTO = new RabbitDTO("1", "Bunny", true, "image.jpg");
        Rabbit rabbit = new Rabbit(rabbitDTO.getId(), rabbitDTO.getName(), rabbitDTO.getStatus(), rabbitDTO.getImagePath());
        when(rabbitService.countRabbit()).thenReturn(10L);
        when(rabbitService.addRabbit(any())).thenReturn(rabbitDTO);

        ApiResponse<RabbitDTO> response = rabbitController.addRabbit(rabbitDTO);

        assertEquals(HttpStatus.OK, response.getSuccess());
        assertEquals("Rabbit added successfully", response.getMessage());
    }

    @Test
    public void testAddRabbitAlreadyExists() {
        RabbitDTO rabbitDTO = new RabbitDTO("1", "Bunny", true, "image.jpg");
        when(rabbitService.countRabbit()).thenReturn(10L);
        when(rabbitService.addRabbit(any())).thenReturn(null);
        when(rabbitRepository.findByName(rabbitDTO.getName())).thenReturn(Collections.singletonList(new Rabbit()));

        ApiResponse<RabbitDTO> response = rabbitController.addRabbit(rabbitDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getSuccess());
        assertEquals("Failed rabbit already exists", response.getMessage());
    }

}
