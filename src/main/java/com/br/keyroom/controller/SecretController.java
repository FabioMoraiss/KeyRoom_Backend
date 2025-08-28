package com.br.keyroom.controller;

import com.br.keyroom.domain.Secret;
import com.br.keyroom.domain.User;
import com.br.keyroom.repository.SecretRepository;
import com.br.keyroom.repository.UserRepository;
import com.br.keyroom.security.TokenService;
import com.br.keyroom.service.ListSecretDTO;
import com.br.keyroom.service.RegisterSecretDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/secret")
public class SecretController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecretRepository secretRepository;

    @PostMapping
    public ResponseEntity<?> postSecret(@RequestHeader("Authorization") String authorizationHeader,
                                        @RequestBody @Validated RegisterSecretDTO secretDTO) {

        String token = getToken(authorizationHeader);
        var login = tokenService.validateToken(token);
        User user = userRepository.findByLogin(login);

        if (user == null) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }

        Secret secret = new Secret(
                user,
                secretDTO.tittle(),
                secretDTO.login(),
                secretDTO.password(),
                secretDTO.url(),
                secretDTO.notes(),
                secretDTO.OTPCode()
        );
        secretRepository.save(secret);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllSecrets(@RequestHeader("Authorization") String authorizationHeader) {
        String token = getToken(authorizationHeader);
        var login = tokenService.validateToken(token);
        User user = userRepository.findByLogin(login);

        if (user == null) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }

        var secrets = secretRepository.findAllByUser(user)
                .stream()
                .map(secret -> new ListSecretDTO(
                        secret.getId(),
                        secret.getTitle(),
                        secret.getLogin(),
                        secret.getPassword(),
                        secret.getUrl(),
                        secret.getNotes(),
                        secret.getOTPCode()
                ))
                .toList();
        return ResponseEntity.ok(secrets);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<?> deleteSecret(@RequestHeader("Authorization") String authorizationHeader,
                                           @PathVariable int id) {
        String token = getToken(authorizationHeader);
        var login = tokenService.validateToken(token);
        User user = userRepository.findByLogin(login);

        if (user == null) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }

        var secret = secretRepository.findById(id);
        if (secret.isEmpty() || !secret.get().getUser().equals(user)) {
            return ResponseEntity.status(404).body("Segredo não encontrado");
        }

        secretRepository.delete(secret.get());
        return ResponseEntity.ok("Secret deleted");
    }

    private String getToken(String authorizationHeader) {
        return authorizationHeader.replace("Bearer ", "");
    }
}

