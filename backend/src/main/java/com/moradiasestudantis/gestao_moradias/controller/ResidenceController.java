package com.moradiasestudantis.gestao_moradias.controller;

import com.moradiasestudantis.gestao_moradias.dto.ResidenceDto;
import com.moradiasestudantis.gestao_moradias.model.Residence;
import com.moradiasestudantis.gestao_moradias.model.User;
import com.moradiasestudantis.gestao_moradias.repository.ResidenceRepository;
import com.moradiasestudantis.gestao_moradias.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/residences")
public class ResidenceController {

    @Autowired
    private ResidenceRepository residenceRepository;

    @Autowired
    private UserRepository userRepository;

    // Tarefa 3.1 & 3.2: Criar uma nova residência
    @PostMapping
    public ResponseEntity<ResidenceDto> createResidence(@Valid @RequestBody ResidenceDto residenceDto) {
        User owner = getCurrentUser();

        Residence residence = new Residence();
        residence.setAddress(residenceDto.getAddress());
        residence.setDescription(residenceDto.getDescription());
        residence.setPrice(residenceDto.getPrice());
        residence.setOwner(owner);

        Residence savedResidence = residenceRepository.save(residence);
        return new ResponseEntity<>(toDto(savedResidence), HttpStatus.CREATED);
    }

    // Tarefa 3.1: Obter uma residência por ID
    @GetMapping("/{id}")
    public ResponseEntity<ResidenceDto> getResidenceById(@PathVariable Long id) {
        Residence residence = residenceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Residência não encontrada"));
        return ResponseEntity.ok(toDto(residence));
    }

    // Tarefa 3.1 & 3.2: Atualizar uma residência
    @PutMapping("/{id}")
    public ResponseEntity<ResidenceDto> updateResidence(@PathVariable Long id, @Valid @RequestBody ResidenceDto residenceDto) {
        User currentUser = getCurrentUser();
        Residence residence = residenceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Residência não encontrada"));

        if (!residence.getOwner().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado. Você não é o proprietário desta residência.");
        }

        residence.setAddress(residenceDto.getAddress());
        residence.setDescription(residenceDto.getDescription());
        residence.setPrice(residenceDto.getPrice());

        Residence updatedResidence = residenceRepository.save(residence);
        return ResponseEntity.ok(toDto(updatedResidence));
    }

    // Tarefa 3.1 & 3.2: Deletar uma residência
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResidence(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        Residence residence = residenceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Residência não encontrada"));

        if (!residence.getOwner().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado. Você não é o proprietário desta residência.");
        }

        residenceRepository.delete(residence);
        return ResponseEntity.noContent().build();
    }

    // Tarefa 3.3: Listar as residências do usuário logado
    @GetMapping("/my-residences")
    public ResponseEntity<List<ResidenceDto>> getMyResidences() {
        User currentUser = getCurrentUser();
        List<Residence> residences = residenceRepository.findByOwner(currentUser);
        List<ResidenceDto> dtos = residences.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Obtém o usuário autenticado no contexto de segurança.
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));
    }

    /**
     * Converte uma entidade Residence para ResidenceDto.
     */
    private ResidenceDto toDto(Residence residence) {
        return new ResidenceDto(
                residence.getId(),
                residence.getAddress(),
                residence.getDescription(),
                residence.getPrice(),
                residence.getOwner().getId()
        );
    }
}
