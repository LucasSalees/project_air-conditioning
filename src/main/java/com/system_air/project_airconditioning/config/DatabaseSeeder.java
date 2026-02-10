package com.system_air.project_airconditioning.config;

import com.system_air.project_airconditioning.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatabaseSeeder {

    @Bean
    public CommandLineRunner seedDatabase(UsuarioRepository repository, PasswordEncoder encoder) {
        return args -> {
            var todosUsuarios = repository.findAll();
            
            todosUsuarios.forEach(usuario -> {
                String senhaAtual = usuario.getPassword();

                // O BCrypt sempre começa com $2a$ ou $2b$
                // Se NÃO começar, significa que é texto puro (como o '123456' vindo do SQL)
                if (!senhaAtual.startsWith("$2a$") && !senhaAtual.startsWith("$2b$")) {
                    System.out.println(">>> Detectada senha em texto puro para: " + usuario.getUsername());
                    
                    usuario.setPassword(encoder.encode(senhaAtual));
                    repository.save(usuario);
                    
                    System.out.println(">>> Senha de [" + usuario.getUsername() + "] criptografada com sucesso!");
                }
            });
        };
    }
}