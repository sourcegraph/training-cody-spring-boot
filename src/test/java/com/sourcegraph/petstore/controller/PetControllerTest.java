package com.sourcegraph.petstore.controller;

import com.sourcegraph.petstore.openapi.generated.model.Pet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PetController.class)
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser // This annotation creates a mock authenticated user
    public void testGetPetById() throws Exception {
        // Define the ID to test with
        long petId = 123L;

        // Execute the GET request
        MvcResult result = mockMvc.perform(get("/api/pets/" + petId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Parse the response
        String responseContent = result.getResponse().getContentAsString();
        Pet responsePet = objectMapper.readValue(responseContent, Pet.class);

        // Verify the pet has the expected ID
        assertNotNull(responsePet);
        assertEquals(petId, responsePet.getId());

        // Additional assertions that could be useful
        assertNotNull(responsePet.getName());
        assertNotNull(responsePet.getPhotoUrls());
        assertNotNull(responsePet.getCategory());
        assertNotNull(responsePet.getStatus());
    }
}