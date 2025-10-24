package com.jackson.demonotes2.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(@RequestParam(required = false, defaultValue = "anónimo") String name) {
        return "Hola " + name + " desde Springboot";
    }
}

