package com.br.keyroom.service.dto;

import org.apache.commons.codec.digest.DigestUtils;


public record PwnedSecretDTO(
                                String tittle,
                                String login,
                                String password,
                                String hashSH1Password) {

    public static String sha1(String input) {
        return DigestUtils.sha1Hex(input);
    }
}
