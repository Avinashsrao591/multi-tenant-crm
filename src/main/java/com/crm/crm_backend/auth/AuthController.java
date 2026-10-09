package com.crm.crm_backend.auth;

import com.crm.crm_backend.dto.LoginRequest;
import com.crm.crm_backend.dto.LoginResponse;
import com.crm.crm_backend.security.JwtService;
import com.crm.crm_backend.user.User;
import com.crm.crm_backend.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getTenant().getId(),
                user.getRole().name(),
                user.getEmail()
        );

        return new LoginResponse(
                token,
                user.getId(),
                user.getTenant().getId(),
                user.getRole().name()
        );
    }
}