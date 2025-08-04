package com.moradiasestudantis.gestao_moradias.controller;

import com.moradiasestudantis.gestao_moradias.model.Residence;
import com.moradiasestudantis.gestao_moradias.security.SecurityFilter;
import com.moradiasestudantis.gestao_moradias.service.ResidenceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResidenceController.class)
class ResidenceControllerTest {

        @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResidenceService residenceService;

    @MockBean
    private SecurityFilter securityFilter;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @WithMockUser(username = "testuser", roles = {"USER"})
    @Test
    void testFilterResidences() throws Exception {
        Residence mockResidence = new Residence();
        mockResidence.setId(1L);
        mockResidence.setEndereco("Rua A");

        when(residenceService.filterResidences("Casa", "Aluguel"))
                .thenReturn(List.of(mockResidence));

        mockMvc.perform(get("/residences/filter")
                .param("tipo", "Casa")
                .param("finalidade", "Aluguel"))
                .andExpect(status().isOk());
    }
    @WithMockUser(username = "testuser", roles = {"USER"})
    @Test
    void testGetAllResidences() throws Exception {
        when(residenceService.findAll()).thenReturn(List.of(new Residence()));
        mockMvc.perform(get("/residences"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "testuser", roles = {"USER"})
    @Test
    void testGetResidenceById() throws Exception {
        Residence mock = new Residence();
        mock.setId(1L);
        mock.setEndereco("Rua A");

        when(residenceService.findById(1L)).thenReturn(mock);

        mockMvc.perform(get("/residences/1"))
                .andExpect(status().isOk());
    }
}
