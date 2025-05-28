package com.br.keyroom.service;
import jakarta.validation.constraints.NotBlank;


public record RegisterSecretDTO(
                                @NotBlank String tittle,
                                @NotBlank String login,
                                @NotBlank String password,
                                String url,
                                String notes,
                                String OTPCode) {
}
