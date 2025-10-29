package com.br.keyroom.service;
import com.br.keyroom.repository.PwnedSecretRepository;
import com.br.keyroom.service.dto.PwnedSecretDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PwnedSecretsService {

    @Autowired
    private PwnedSecretRepository pwnedSecretRepository;

    public List<PwnedSecretDTO> checkSecrets(List<PwnedSecretDTO> pwnedSecrets) {
        return pwnedSecrets.stream()
                .filter(secret -> {
                    String hash = secret.hashSH1Password().toUpperCase();
                    String prefix = hash.substring(0, 5);
                    String suffix = hash.substring(5);
                    return pwnedSecretRepository.isPasswordPwned(prefix, suffix);
                })
                .collect(Collectors.toList());
    }


}
