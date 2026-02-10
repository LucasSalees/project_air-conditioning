package com.system_air.project_airconditioning.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Entity
@Table(name = "empresas")
@Getter 
@Setter
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100) 
    private String nome;

    @Column(nullable = false, unique = true, length = 14) 
    private String cnpj;

    // Se quiser que o JPA gerencie a deleção:
    // @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<Usuario> usuarios;
    
    // Adicione isso manualmente na classe Empresa
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}