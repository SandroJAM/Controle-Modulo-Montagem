package com.sandrojam.modulomontagem.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Autentica requisicoes recebidas do ERP no webhook de entrada via header
 * "X-Api-Key", sem exigir login de usuario. Aplica-se apenas as rotas sob
 * /api/webhooks/**.
 */
@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    @Value("${app.erp.webhook-api-key}")
    private String apiKeyEsperada;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                     @NonNull HttpServletResponse response,
                                     @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/api/webhooks/")) {
            String apiKeyRecebida = request.getHeader("X-Api-Key");

            if (apiKeyRecebida != null && apiKeyRecebida.equals(apiKeyEsperada)) {
                var authToken = new UsernamePasswordAuthenticationToken(
                        "erp-integracao", null, List.of(new SimpleGrantedAuthority("ROLE_ERP_INTEGRACAO")));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "X-Api-Key invalida ou ausente");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
