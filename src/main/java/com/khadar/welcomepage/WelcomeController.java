package com.example.welcomepage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Controller
public class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "welcome"; // Refers to welcome.html (if using Thymeleaf)
    }
}
