package com.system_air.project_airconditioning.controller;

import com.system_air.project_airconditioning.model.Agendamento;
import com.system_air.project_airconditioning.repository.AgendamentoRepository;
import com.system_air.project_airconditioning.repository.EmpresaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoRepository repository;
    
    @Autowired
    private EmpresaRepository empresaRepository; 

    @GetMapping
    public List<Agendamento> listar(@RequestAttribute("empresaId") Long empresaId) {
        // Agora buscamos apenas os agendamentos daquela empresa específica
        return repository.findAllByEmpresaId(empresaId);
    }

    @PostMapping
    public ResponseEntity<Agendamento> salvar(@RequestBody Agendamento agendamento, @RequestAttribute("empresaId") Long empresaId) {
        // Agora o getReferenceById retornará uma Empresa (devido ao conserto no Repository)
        var empresa = empresaRepository.getReferenceById(empresaId);
        
        // O Java agora aceitará esta linha perfeitamente
        agendamento.setEmpresa(empresa);
        
        var salvo = repository.save(agendamento);
        return ResponseEntity.ok(salvo);
    }
}