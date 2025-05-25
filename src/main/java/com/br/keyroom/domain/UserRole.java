package com.br.keyroom.domain;

public enum UserRole {
    ADMIN("admim"),
    USER("user");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
