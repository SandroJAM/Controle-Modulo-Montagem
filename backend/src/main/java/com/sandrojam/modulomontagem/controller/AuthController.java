package com.sandrojam.modulomontagem.controller;

import com.sandrojam.modulomontagem.dto.LoginRequestDTO;
import com.sandrojam.modulomontagem.dto.LoginResponseDTO;
import com.sandrojam.modulomontagem.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }
}
