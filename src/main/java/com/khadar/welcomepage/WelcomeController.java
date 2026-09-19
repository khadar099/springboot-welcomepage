package com.khadar.welcomepage;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WelcomeController {

    private final UserService userService;

    public WelcomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String mobile,
            @RequestParam String password,
            Model model) {

        String result = userService.registerUser(
                fullName,
                email,
                mobile,
                password
        );

        if ("INVALID_EMAIL".equals(result)) {
            model.addAttribute(
                    "error",
                    "Please enter a correct email address."
            );
            return "signup";
        }

        if ("EMAIL_EXISTS".equals(result)) {
            model.addAttribute(
                    "error",
                    "Email is already registered."
            );
            return "signup";
        }

        if ("SUCCESS".equals(result)) {
            return "redirect:/login?signupSuccess=true";
        }

        model.addAttribute(
                "error",
                "Something went wrong. Please try again."
        );

        return "signup";
    }

    @GetMapping("/track-order")
    public String trackOrder() {
        return "track-order";
    }
}
