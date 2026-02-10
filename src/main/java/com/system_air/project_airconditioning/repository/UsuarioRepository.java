package com.system_air.project_airconditioning.repository;

import com.system_air.project_airconditioning.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Busca o usuário e já traz a empresa (Join Fetch) para evitar o problema do N+1
    Optional<Usuario> findByUsername(String username);
}