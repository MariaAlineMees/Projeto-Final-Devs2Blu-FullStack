package com.roteiro.roteiro_service.service;

import com.roteiro.roteiro_service.dtos.RoteiroCriadoDTO; // Import corrigido
import com.roteiro.roteiro_service.model.Roteiro;
import com.roteiro.roteiro_service.model.User;
import com.roteiro.roteiro_service.repository.RoteiroRepository;
import com.roteiro.roteiro_service.repository.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoteiroService {

    @Autowired
    private RoteiroRepository roteiroRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.queue.roteiro.criado}")
    private String queueRoteiroCriado;

    public Roteiro save(Roteiro roteiro) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        roteiro.setUser(user);
        
        Roteiro novoRoteiro = roteiroRepository.save(roteiro);


        RoteiroCriadoDTO roteiroDTO = new RoteiroCriadoDTO();
        roteiroDTO.setId(novoRoteiro.getId());
        roteiroDTO.setTitulo(novoRoteiro.getTitulo());
        roteiroDTO.setDestino(novoRoteiro.getDestino());
        

        roteiroDTO.setDataInicio(novoRoteiro.getDataInicio());
        roteiroDTO.setDataFim(novoRoteiro.getDataFim());
        roteiroDTO.setCustoEstimado(novoRoteiro.getCustoEstimado());
        
        roteiroDTO.setUserEmail(user.getEmail());
        roteiroDTO.setUsername(user.getUsername());
        
        rabbitTemplate.convertAndSend(queueRoteiroCriado, roteiroDTO);

        return novoRoteiro;
    }


    public List<Roteiro> findAll() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return roteiroRepository.findByUser(user);
    }

    public Roteiro findById(Long id) {
        return roteiroRepository.findById(id).orElseThrow(() -> new RuntimeException("Roteiro não encontrado"));
    }

    public Roteiro update(Long id, Roteiro roteiroDetails) {
        Roteiro roteiro = findById(id);
        roteiro.setTitulo(roteiroDetails.getTitulo());
        roteiro.setDestino(roteiroDetails.getDestino());
        roteiro.setDataInicio(roteiroDetails.getDataInicio());
        roteiro.setDataFim(roteiroDetails.getDataFim());
        roteiro.setCustoEstimado(roteiroDetails.getCustoEstimado());
        return roteiroRepository.save(roteiro);
    }

    public void delete(Long id) {
        Roteiro roteiro = findById(id);
        roteiroRepository.delete(roteiro);
    }
}
