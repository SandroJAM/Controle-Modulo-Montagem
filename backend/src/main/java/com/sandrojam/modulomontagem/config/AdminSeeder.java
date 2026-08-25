package com.sandrojam.modulomontagem.config;

import com.sandrojam.modulomontagem.model.PerfilUsuarioMontagem;
import com.sandrojam.modulomontagem.model.Usuario;
import com.sandrojam.modulomontagem.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Garante que exista pelo menos um usuario ADMIN quando a aplicacao sobe
 * pela primeira vez, para resolver o problema de "ovo e a galinha": criar
 * usuario exige estar autenticado como ADMIN, mas sem nenhum usuario no
 * banco ninguem consegue logar.
 *
 * So cria o admin se NENHUM usuario existir ainda no banco -- em qualquer
 * execucao seguinte, nao faz nada. As credenciais iniciais vem de
 * variaveis de ambiente (com um default apenas para ambiente local); a
 * senha e sempre gravada com o mesmo PasswordEncoder (BCrypt) usado pelo
 * resto do sistema, nunca em texto puro nem hash colado a mao.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AdminSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin-inicial.nome:Administrador}")
    private String nomeInicial;

    @Value("${app.admin-inicial.email:admin@modulomontagem.local}")
    private String emailInicial;

    @Value("${app.admin-inicial.senha:troque-esta-senha}")
    private String senhaInicial;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            return; // ja existe pelo menos um usuario; nao mexe em nada
        }

        Usuario admin = Usuario.builder()
                .nome(nomeInicial)
                .email(emailInicial)
                .senhaHash(passwordEncoder.encode(senhaInicial))
                .perfil(PerfilUsuarioMontagem.ADMIN)
                .ativo(true)
                .build();

        usuarioRepository.save(admin);

        log.warn("Nenhum usuario encontrado no banco. Usuario ADMIN inicial criado: {} "
                + "-- faca login e troque a senha (ou crie seu admin definitivo e desative este) o quanto antes.",
                emailInicial);
    }
}
