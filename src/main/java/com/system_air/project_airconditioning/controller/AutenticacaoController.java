package com.system_air.project_airconditioning.controller;

import com.system_air.project_airconditioning.model.Usuario;
import com.system_air.project_airconditioning.repository.UsuarioRepository;
import com.system_air.project_airconditioning.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<?> efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        try {
            // O Spring Security já valida se o usuário existe e se a senha está correta
            var authenticationToken = new UsernamePasswordAuthenticationToken(dados.username(), dados.password());
            var authentication = manager.authenticate(authenticationToken);
            
            var usuarioLogado = (Usuario) authentication.getPrincipal();
            var tokenJWT = tokenService.gerarToken(usuarioLogado);

            // Retorna o Token e o status de primeiro acesso para o React
            return ResponseEntity.ok(new DadosTokenJWT(tokenJWT, usuarioLogado.isPrimeiroAcesso()));
            
        } catch (Exception e) {
            // Retornamos 403 para credenciais erradas, mantendo a segurança
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usuário ou senha inválidos");
        }
    }

    @PutMapping("/alterar-senha")
    public ResponseEntity<?> alterarSenha(@RequestBody @Valid DadosAlteracaoSenha dados) {
        try {
            // Recupera o usuário autenticado do contexto do Spring Security
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            var usuario = (Usuario) authentication.getPrincipal();
            
            // Criptografa a nova senha usando o Bean injetado
            usuario.setPassword(passwordEncoder.encode(dados.novaSenha()));
            usuario.setPrimeiroAcesso(false);
            
            repository.save(usuario);
            return ResponseEntity.noContent().build();
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao processar alteração");
        }
    }
    
    @GetMapping("/health") // O caminho final será /login/health
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("CLASS_AR_SYSTEM_READY");
    }

    // --- DTOs (Data Transfer Objects) ---
    public record DadosAutenticacao(String username, String password) {}
    public record DadosTokenJWT(String token, boolean primeiroAcesso) {}
    public record DadosAlteracaoSenha(String novaSenha) {}
}