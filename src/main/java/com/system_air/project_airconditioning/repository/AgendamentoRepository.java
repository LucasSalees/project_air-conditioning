package com.system_air.project_airconditioning.repository;
import com.system_air.project_airconditioning.model.Agendamento;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findAllByEmpresaId(Long empresaId);
}