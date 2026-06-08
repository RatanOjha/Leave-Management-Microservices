
package com.lms.authservice.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lms.authservice.dto.LoginRequest;
import com.lms.authservice.dto.RegisterRequest;
import com.lms.authservice.entity.User;
import com.lms.authservice.repository.UserRepository;
import com.lms.authservice.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public String register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {

            return "User already exists";
        }

        User user = User.builder().username(request.getUsername()).password(passwordEncoder.encode(request
            .getPassword())).role(request.getRole()).build();

        userRepository.save(user);

        return "User Registered Successfully";
    }

    // login

    public String login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new RuntimeException(
            "Invalid Username"));

        boolean valid = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!valid) {

            throw new RuntimeException("Invalid Password");
        }

        return jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
    }

}
