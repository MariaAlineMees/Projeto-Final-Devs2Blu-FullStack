package com.roteiro.roteiro_service.service;

import com.roteiro.roteiro_service.model.Roteiro;
import com.roteiro.roteiro_service.model.User;
import com.roteiro.roteiro_service.repository.RoteiroRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger; // Importar Logger
import org.slf4j.LoggerFactory; // Importar LoggerFactory

import java.util.List;

@Service
public class RoteiroService {

    private static final Logger log = LoggerFactory.getLogger(RoteiroService.class); // Adicionar Logger

    private final RoteiroRepository roteiroRepository;
    private final RabbitTemplate rabbitTemplate;

    public RoteiroService(RoteiroRepository roteiroRepository, RabbitTemplate rabbitTemplate) {
        this.roteiroRepository = roteiroRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<Roteiro> findAll() {
        User user = getCurrentUser();
        return roteiroRepository.findByUser(user);
    }

    public Roteiro save(Roteiro roteiro) {
        User user = getCurrentUser();
        roteiro.setUser(user); // Associa o roteiro ao usuário logado
        Roteiro savedRoteiro = roteiroRepository.save(roteiro);
        log.info("Sending Roteiro created message to RabbitMQ: {}", savedRoteiro); // Adicionar log
        rabbitTemplate.convertAndSend("roteiro.criado.queue", savedRoteiro);
        return savedRoteiro;
    }

    public Roteiro update(Long id, Roteiro roteiroDetails) {
        User user = getCurrentUser();
        return roteiroRepository.findByIdAndUser(id, user)
                .map(roteiro -> {
                    roteiro.setTitulo(roteiroDetails.getTitulo());
                    roteiro.setDestino(roteiroDetails.getDestino());
                    roteiro.setDataInicio(roteiroDetails.getDataInicio());
                    roteiro.setDataFim(roteiroDetails.getDataFim());
                    roteiro.setCustoEstimado(roteiroDetails.getCustoEstimado());
                    return roteiroRepository.save(roteiro);
                }).orElseThrow(() -> new RuntimeException("Roteiro não encontrado ou não pertence ao usuário"));
    }

    public void delete(Long id) {
        User user = getCurrentUser();
        Roteiro roteiro = roteiroRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Roteiro não encontrado ou não pertence ao usuário"));
        roteiroRepository.delete(roteiro);
    }

    private User getCurrentUser() {
        // Pega o usuário autenticado do contexto de segurança do Spring
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
