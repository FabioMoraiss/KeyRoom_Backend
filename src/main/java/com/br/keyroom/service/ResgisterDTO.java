package com.br.keyroom.service;


import com.br.keyroom.domain.UserRole;

public record ResgisterDTO(String login, String password, UserRole role) {
}
