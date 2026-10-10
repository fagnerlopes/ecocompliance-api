package br.com.fiap.ecocompliance.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UpController {

    @GetMapping("/up")
    public Map<String, String> verificar() {
        return Map.of("status", "up");
    }
}
