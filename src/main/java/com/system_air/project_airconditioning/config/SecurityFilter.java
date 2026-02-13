package com.system_air.project_airconditioning.config;

import com.system_air.project_airconditioning.repository.UsuarioRepository;
import com.system_air.project_airconditioning.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        // ESCAPE: Se for health ou login, ignora o filtro e vai direto para o próximo
        if (path.contains("/login/health") || path.contains("/auth") || path.contains("/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            try {
                String subject = tokenService.getSubject(tokenJWT);
                Long empresaId = tokenService.getClaim(tokenJWT, "empresaId");
                request.setAttribute("empresaId", empresaId);

                var usuario = repository.findByUsername(subject)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // Se o token for inválido, não trava a requisição aqui, 
                // deixa o Spring Security barrar no 'authenticated()' do SecurityConfig
                System.err.println("Erro na validação do token: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}