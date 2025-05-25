package com.br.keyroom.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/hello")
    public String createContact() {
        return "oi, eu sou desprotegido ";
    }

    @PostMapping("/hello")
    public String createContactPost() {
        return "oi, eu sou protegido ";
    }
}
