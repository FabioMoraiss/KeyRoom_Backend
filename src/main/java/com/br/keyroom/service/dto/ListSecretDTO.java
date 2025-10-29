package com.br.keyroom.service.dto;


public record ListSecretDTO(    int id,
                                String tittle,
                                String login,
                                String password,
                                String url,
                                String notes,
                                String OTPCode) {
}
