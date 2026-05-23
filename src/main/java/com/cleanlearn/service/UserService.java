package com.cleanlearn.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.cleanlearn.repository.UserRepository;
import com.cleanlearn.entity.User;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User validateUserById(Long userId) throws Exception {
        return userRepository.findById(userId).orElseThrow(() -> new Exception("User not found with id: " + userId));
    }

}