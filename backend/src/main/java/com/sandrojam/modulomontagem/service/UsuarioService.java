package com.sandrojam.modulomontagem.service;

import com.sandrojam.modulomontagem.dto.UsuarioDTO;
import com.sandrojam.modulomontagem.exception.RegraNegocioException;
import com.sandrojam.modulomontagem.exception.ResourceNotFoundException;
import com.sandrojam.modulomontagem.model.Usuario;
import com.sandrojam.modulomontagem.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioDTO criar(UsuarioDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new RegraNegocioException("Ja existe um usuario com este e-mail.");
        }
        if (dto.senha() == null || dto.senha().isBlank()) {
            throw new RegraNegocioException("Senha e obrigatoria na criacao do usuario.");
        }

        Usuario usuario = Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senhaHash(passwordEncoder.encode(dto.senha()))
                .perfil(dto.perfil())
                .ativo(true)
                .build();

        return toDTO(usuarioRepository.save(usuario));
    }

    public List<UsuarioDTO> listar() {
        return usuarioRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional
    public UsuarioDTO atualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = buscarOuFalhar(id);

        usuario.setNome(dto.nome());
        usuario.setPerfil(dto.perfil());
        usuario.setAtivo(dto.ativo());

        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuario.setSenhaHash(passwordEncoder.encode(dto.senha()));
        }

        return toDTO(usuarioRepository.save(usuario));
    }

    private Usuario buscarOuFalhar(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado: " + id));
    }

    private UsuarioDTO toDTO(Usuario u) {
        return new UsuarioDTO(u.getId(), u.getNome(), u.getEmail(), null, u.getPerfil(), u.isAtivo());
    }
}
