package com.goodmorning.controller;

import com.goodmorning.entity.User;
import com.goodmorning.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/auth")
    public ResponseEntity<String> authenticate(
            @RequestParam String email,
            @RequestParam String password
    ) {

        log.debug("Authentication request received for email: {}", email);

        Optional<User> optionalUser =
                userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {

            User user = optionalUser.get();

            log.debug("User found in database");

            if (user.getPassword().equals(password)) {

                log.info("Authentication successful for email: {}", email);

                return ResponseEntity.ok(
                        "Authentication Successful"
                );
            }
        }

        log.warn("Authentication failed for email: {}", email);

        return ResponseEntity
                .badRequest()
                .body("Invalid Email or Password");
    }
}
