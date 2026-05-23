package com.cleanlearn.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import com.cleanlearn.entity.User;
import com.cleanlearn.security.JwtUtils;
import com.cleanlearn.repository.UserRepository;
import com.cleanlearn.service.EmailService;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user")
    public ResponseEntity<String> getUserInfo(@RequestParam String email) {
        
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            log.error("User info :{}", user);
            return ResponseEntity.ok(user.toString());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

}