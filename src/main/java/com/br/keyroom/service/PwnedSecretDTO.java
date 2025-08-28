package com.br.keyroom.service;
import jakarta.validation.constraints.NotBlank;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
