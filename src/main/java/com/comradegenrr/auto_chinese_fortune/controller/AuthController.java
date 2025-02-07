package com.comradegenrr.auto_chinese_fortune.controller;

import com.comradegenrr.auto_chinese_fortune.config.UserAlreadyExistException;
import com.comradegenrr.auto_chinese_fortune.config.UserNotAvilableException;
import com.comradegenrr.auto_chinese_fortune.config.UserNotExistException;
import com.comradegenrr.auto_chinese_fortune.config.WrongPasswordException;
import com.comradegenrr.auto_chinese_fortune.dto.AuthResponse;
import com.comradegenrr.auto_chinese_fortune.dto.LoginRequest;
import com.comradegenrr.auto_chinese_fortune.dto.RegisterRequest;
import com.comradegenrr.auto_chinese_fortune.model.User;
import com.comradegenrr.auto_chinese_fortune.repository.UserRepository;
import com.comradegenrr.auto_chinese_fortune.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request)
            throws BadCredentialsException, UserNotAvilableException, UserNotExistException, WrongPasswordException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        log.info("User {} logged in", request.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) throws UserAlreadyExistException {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);

        // 保存用户到数据库
        userRepository.findByUsername(user.getUsername()).ifPresent(u -> {
            throw new UserAlreadyExistException("User already exists");
        });
        userRepository.save(user);
        log.info("User {} registered", request.getUsername());
        return ResponseEntity.ok("Registration successful");
    }
}