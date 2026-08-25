package com.sandrojam.modulomontagem.service;

import com.sandrojam.modulomontagem.dto.LoginRequestDTO;
import com.sandrojam.modulomontagem.dto.LoginResponseDTO;
import com.sandrojam.modulomontagem.model.Usuario;
import com.sandrojam.modulomontagem.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponseDTO login(LoginRequestDTO request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha()));

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String token = jwtService.generateToken(usuario);

        return new LoginResponseDTO(token, usuario.getNome(), usuario.getPerfil().name());
    }
}
