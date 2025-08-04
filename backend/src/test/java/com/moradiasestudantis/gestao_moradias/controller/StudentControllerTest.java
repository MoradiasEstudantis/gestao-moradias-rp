package com.moradiasestudantis.gestao_moradias.controller;

import com.moradiasestudantis.gestao_moradias.dto.StudentDto;
import com.moradiasestudantis.gestao_moradias.dto.StudentFilterDto;
import com.moradiasestudantis.gestao_moradias.security.SecurityConfig;
import com.moradiasestudantis.gestao_moradias.security.SecurityFilter;
import com.moradiasestudantis.gestao_moradias.security.TokenService;
import com.moradiasestudantis.gestao_moradias.service.StudentService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private SecurityFilter securityFilter;

    @Test
    @WithMockUser(roles = "PROPRIETARIO")
    void testSearchStudents_ComRoleProprietario_DeveRetornar200() throws Exception {
        StudentDto student = new StudentDto(
                1L,
                "João da Silva",
                "12345678901",
                LocalDate.of(2000, 1, 1),
                "61999999999",
                "5º período",
                "Engenharia",
                true,
                true,
                true,
                true
        );

        Mockito.when(studentService.searchStudents(any(StudentFilterDto.class)))
                .thenReturn(List.of(student));

        mockMvc.perform(get("/students/search")
                        .param("wifi", "true")
                        .param("garagem", "true")
                        .param("banheiroPrivativo", "true"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "usuario", roles = "USER") 
    @Test
    void testSearchStudents_SemPermissao_DeveRetornar403() throws Exception {
        mockMvc.perform(get("/students/search"))
           .andExpect(status().isForbidden()); // Espera 403
    }

    @WithMockUser(roles = "PROPRIETARIO")
    @Test
    void testSearchStudents_ComRoleProprietario_DeveRetornar200_Again() throws Exception {
        mockMvc.perform(get("/students/search"))
           .andExpect(status().isOk());
    }

}
