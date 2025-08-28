package com.br.keyroom.service;
import jakarta.validation.constraints.NotBlank;


public record ListSecretDTO(    int id,
                                String tittle,
                                String login,
                                String password,
                                String url,
                                String notes,
                                String OTPCode) {
}
