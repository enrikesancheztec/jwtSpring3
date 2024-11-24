package com.example.jwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HolaController {
  @GetMapping("/hola")
  public String hola() {
    return "¡Hola!";
  }

  @GetMapping("/hello")
  public String hello() {
    return "Hello!";
  }
}
