package com.br.keyroom.controller;

import com.br.keyroom.domain.Secret;
import com.br.keyroom.domain.User;
import com.br.keyroom.repository.SecretRepository;
import com.br.keyroom.repository.UserRepository;
import com.br.keyroom.security.TokenService;
import com.br.keyroom.service.ListSecretDTO;
import com.br.keyroom.service.PwnedSecretDTO;
import com.br.keyroom.service.PwnedSecretsService;
import com.br.keyroom.service.RegisterSecretDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/secret")
public class SecretController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecretRepository secretRepository;

    @Autowired
    private PwnedSecretsService pwnedSecretsService;

    private static final String ERROR_DELETE_SECRET = "Error deleting secret";
    private static final String USER_NOT_FOUND = "User not found";
    private static final String ERROR_UPDATING_SECRET = "Error updating secret";


    @PostMapping
    public ResponseEntity<?> postSecret(@RequestHeader("Authorization") String authorizationHeader,
                                        @RequestBody @Validated RegisterSecretDTO secretDTO) {

        String token = getToken(authorizationHeader);
        var login = tokenService.validateToken(token);
        User user = userRepository.findByLogin(login);

        if (user == null) {
            return ResponseEntity.status(404).body(USER_NOT_FOUND);
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
            return ResponseEntity.status(404).body(USER_NOT_FOUND);
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
            return ResponseEntity.status(404).body(ERROR_DELETE_SECRET);
        }

        var secret = secretRepository.findById(id);
        if (secret.isEmpty() || !secret.get().getUser().equals(user)) {
            return ResponseEntity.status(404).body(ERROR_DELETE_SECRET);
        }

        secretRepository.delete(secret.get());
        return ResponseEntity.ok("Secret deleted");
    }

    private String getToken(String authorizationHeader) {
        return authorizationHeader.replace("Bearer ", "");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSecret(@RequestHeader("Authorization") String authorizationHeader,
                                          @PathVariable int id,
                                          @RequestBody @Validated RegisterSecretDTO secretDTO) {
        String token = getToken(authorizationHeader);
        var login = tokenService.validateToken(token);
        User user = userRepository.findByLogin(login);

        if (user == null) {
            return ResponseEntity.status(404).body(USER_NOT_FOUND);
        }

        var secret = secretRepository.findById(id);
        if (secret.isEmpty() || !secret.get().getUser().equals(user)) {
            return ResponseEntity.status(404).body(ERROR_UPDATING_SECRET);
        }

        Secret secretUpdated = secret.get();
        secretUpdated.setTitle(secretDTO.tittle());
        secretUpdated.setLogin(secretDTO.login());
        secretUpdated.setPassword(secretDTO.password());
        secretUpdated.setUrl(secretDTO.url());
        secretUpdated.setNotes(secretDTO.notes());
        secretUpdated.setOTPCode(secretDTO.OTPCode());

        secretRepository.save(secretUpdated);

        return ResponseEntity.ok("Secret updated");
    }

    @GetMapping("/haveibeenpwned")
    public ResponseEntity<?> haveIBeenPwned(@RequestHeader("Authorization") String authorizationHeader) {
        String token = getToken(authorizationHeader);
        var login = tokenService.validateToken(token);
        User user = userRepository.findByLogin(login);

        if (user == null) {
            return ResponseEntity.status(404).body(USER_NOT_FOUND);
        }

        var secrets = secretRepository.findAllByUser(user);
        List<PwnedSecretDTO> pwnedSecrets = secrets.stream()
                .map(secret -> new PwnedSecretDTO(
                        secret.getTitle(),
                        secret.getLogin(),
                        secret.getPassword(),
                        PwnedSecretDTO.sha1(secret.getPassword())
                ))
                .toList();


      var truepwedScrets = pwnedSecretsService.checkSecrets(pwnedSecrets);


        return ResponseEntity.ok(truepwedScrets);
    }

}

