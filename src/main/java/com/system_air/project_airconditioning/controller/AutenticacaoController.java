package com.system_air.project_airconditioning.controller;

import com.system_air.project_airconditioning.model.Usuario;
import com.system_air.project_airconditioning.repository.UsuarioRepository;
import com.system_air.project_airconditioning.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "*") 
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        try {
            System.out.println(">>> TENTATIVA DE LOGIN: " + dados.username());
            
            // 1. Busca o usuário para conferência de segurança e logs
            Usuario usuarioNoBanco = repository.findByUsername(dados.username())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            
            // 2. Validação via Spring Security
            var authenticationToken = new UsernamePasswordAuthenticationToken(dados.username(), dados.password());
            var authentication = manager.authenticate(authenticationToken);
            
            // 3. Geração do Token para o usuário autenticado
            var usuarioLogado = (Usuario) authentication.getPrincipal();
            var tokenJWT = tokenService.gerarToken(usuarioLogado);
            
            System.out.println(">>> LOGIN SUCESSO! PRIMEIRO ACESSO: " + usuarioLogado.isPrimeiroAcesso());

            // Retorna o Token e o status de primeiro acesso para o React
            return ResponseEntity.ok(new DadosTokenJWT(tokenJWT, usuarioLogado.isPrimeiroAcesso()));
            
        } catch (Exception e) {
            System.out.println(">>> ERRO NO LOGIN: " + e.getMessage());
            return ResponseEntity.status(403).body("Erro na autenticação: " + e.getMessage());
        }
    }

    @PutMapping("/alterar-senha")
    public ResponseEntity alterarSenha(@RequestBody @Valid DadosAlteracaoSenha dados) {
        try {
            // Pega o usuário que já está autenticado pelo Token enviado no Header
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            var usuario = (Usuario) authentication.getPrincipal();
            
            // Criptografa a nova senha escolhida pelo usuário
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            usuario.setPassword(encoder.encode(dados.novaSenha()));
            
            // Marca que o usuário já passou pelo primeiro acesso
            usuario.setPrimeiroAcesso(false);
            
            repository.save(usuario);
            
            System.out.println(">>> SENHA ALTERADA COM SUCESSO PARA: " + usuario.getUsername());
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erro ao alterar senha: " + e.getMessage());
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Servidor Online");
    }

    // --- DTOs (Data Transfer Objects) ---

    public record DadosAutenticacao(String username, String password) {}

    public record DadosTokenJWT(String token, boolean primeiroAcesso) {}

    public record DadosAlteracaoSenha(String novaSenha) {}
}