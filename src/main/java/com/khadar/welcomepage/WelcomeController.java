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

    // ==========================
    // WELCOME PAGE
    // ==========================
    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }

    // ==========================
    // LOGIN PAGE
    // ==========================
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // ==========================
    // LOGIN
    // ==========================
    @PostMapping("/login")
    public String loginUser(
            @RequestParam String email,
            @RequestParam String password,
            Model model) {

        String result = userService.loginUser(email, password);

        // Login successful
        if ("SUCCESS".equals(result)) {
            return "redirect:/shop";
        }

        // User is not registered
        if ("USER_NOT_REGISTERED".equals(result)) {
            model.addAttribute(
                    "error",
                    "User is not registered. Please sign up first."
            );
            return "login";
        }

        // Wrong password
        if ("INVALID_PASSWORD".equals(result)) {
            model.addAttribute(
                    "error",
                    "Invalid password. Please try again."
            );
            return "login";
        }

        // Unexpected error
        model.addAttribute(
                "error",
                "Something went wrong. Please try again."
        );

        return "login";
    }

    // ==========================
    // SIGNUP PAGE
    // ==========================
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    // ==========================
    // SIGNUP
    // ==========================
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

        // Invalid email
        if ("INVALID_EMAIL".equals(result)) {
            model.addAttribute(
                    "error",
                    "Please enter a correct email address."
            );
            return "signup";
        }

        // Email already registered
        if ("EMAIL_EXISTS".equals(result)) {
            model.addAttribute(
                    "error",
                    "Email is already registered."
            );
            return "signup";
        }

        // Signup successful
        if ("SUCCESS".equals(result)) {
            return "redirect:/login?signupSuccess=true";
        }

        // Unexpected error
        model.addAttribute(
                "error",
                "Something went wrong. Please try again."
        );

        return "signup";
    }

    // ==========================
    // SHOP PAGE
    // ==========================
    @GetMapping("/shop")
    public String shop() {
        return "shop";
    }

    // ==========================
    // TRACK ORDER
    // ==========================
    @GetMapping("/track-order")
    public String trackOrder() {
        return "track-order";
    }
}
