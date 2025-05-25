package com.br.keyroom.controller;


import com.br.keyroom.domain.User;
import com.br.keyroom.domain.UserRole;
import com.br.keyroom.repository.UserRepository;
import com.br.keyroom.security.TokenService;
import com.br.keyroom.service.AuthenticationDTO;
import com.br.keyroom.service.ResgisterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Validated AuthenticationDTO authenticationDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.login(), authenticationDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = this.tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Validated ResgisterDTO resgisterDTO) {
        if(this.userRepository.findByLogin(resgisterDTO.login()) != null) {
            return ResponseEntity.badRequest().build();
        }
        String encodedPassword = new BCryptPasswordEncoder().encode(resgisterDTO.password());
        User newUser = new User(resgisterDTO.login(), encodedPassword, UserRole.USER);
        this.userRepository.save(newUser);
        return ResponseEntity.ok().build();
    }
}
