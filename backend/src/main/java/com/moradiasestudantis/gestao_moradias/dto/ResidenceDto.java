package com.moradiasestudantis.gestao_moradias.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class ResidenceDto {

    private Long id;

    @NotBlank(message = "O endereço é obrigatório.")
    private String address;

    private String description;

    @PositiveOrZero(message = "O preço deve ser um valor positivo ou zero.")
    private double price;

    private Long ownerId; // Apenas para visualização

    // Construtores
    public ResidenceDto() {
    }

    public ResidenceDto(Long id, String address, String description, double price, Long ownerId) {
        this.id = id;
        this.address = address;
        this.description = description;
        this.price = price;
        this.ownerId = ownerId;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
