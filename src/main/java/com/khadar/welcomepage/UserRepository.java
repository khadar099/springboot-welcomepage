package com.khadar.welcomepage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Check whether an email is already registered
    boolean existsByEmailIgnoreCase(String email);

    // Find a user by email for login
    Optional<User> findByEmailIgnoreCase(String email);
}
