package com.khadar.welcomepage;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String registerUser(String fullName,
                               String email,
                               String mobile,
                               String password) {

        // Remove extra spaces
        fullName = fullName == null ? "" : fullName.trim();
        email = email == null ? "" : email.trim().toLowerCase();
        mobile = mobile == null ? "" : mobile.trim();
        password = password == null ? "" : password;

        // Validate email
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return "INVALID_EMAIL";
        }

        // Check duplicate email
        if (userRepository.existsByEmailIgnoreCase(email)) {
            return "EMAIL_EXISTS";
        }

        // Create new user
        User user = new User();

        user.setFullName(fullName);
        user.setEmail(email);
        user.setMobile(mobile);

        // Never store the password as plain text
        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);

        // Save user to H2
        userRepository.save(user);

        return "SUCCESS";
    }
}
