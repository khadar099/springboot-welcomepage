package com.khadar.welcomepage;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // ==========================
    // SIGNUP
    // ==========================
    public String registerUser(String fullName,
                               String email,
                               String mobile,
                               String password) {

        fullName = fullName == null ? "" : fullName.trim();
        email = email == null ? "" : email.trim().toLowerCase();
        mobile = mobile == null ? "" : mobile.trim();
        password = password == null ? "" : password;

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return "INVALID_EMAIL";
        }

        if (userRepository.existsByEmailIgnoreCase(email)) {
            return "EMAIL_EXISTS";
        }

        User user = new User();

        user.setFullName(fullName);
        user.setEmail(email);
        user.setMobile(mobile);

        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);

        userRepository.save(user);

        return "SUCCESS";
    }

    // ==========================
    // LOGIN
    // ==========================
    public String loginUser(String email, String password) {

        email = email == null ? "" : email.trim().toLowerCase();
        password = password == null ? "" : password;

        Optional<User> userOptional =
                userRepository.findByEmailIgnoreCase(email);

        // User does not exist
        if (userOptional.isEmpty()) {
            return "USER_NOT_REGISTERED";
        }

        User user = userOptional.get();

        // User exists, but password is incorrect
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "INVALID_PASSWORD";
        }

        // Login successful
        return "SUCCESS";
    }
}
