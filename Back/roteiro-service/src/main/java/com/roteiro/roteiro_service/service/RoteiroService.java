package com.roteiro.roteiro_service.service;

import com.roteiro.roteiro_service.model.Roteiro;
import com.roteiro.roteiro_service.model.User;
import com.roteiro.roteiro_service.repository.RoteiroRepository;
import com.roteiro.roteiro_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Roteiro save(Roteiro roteiro) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        roteiro.setUser(user);
        return roteiroRepository.save(roteiro);
    }

    // CORRIGIDO: Agora filtra os roteiros pelo usuário logado
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
