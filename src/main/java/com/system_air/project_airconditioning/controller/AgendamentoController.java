package com.system_air.project_airconditioning.controller;

import com.system_air.project_airconditioning.model.Agendamento;
import com.system_air.project_airconditioning.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoRepository repository;

    @GetMapping
    public List<Agendamento> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Agendamento salvar(@RequestBody Agendamento agendamento) {
        return repository.save(agendamento);
    }
}