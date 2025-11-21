package com.roteiro.roteiro_service.controller;

import com.roteiro.roteiro_service.dtos.SuggestionRequest;
import com.roteiro.roteiro_service.dtos.SuggestionResponse;
import com.roteiro.roteiro_service.model.Roteiro;
import com.roteiro.roteiro_service.service.RoteiroService;
import com.roteiro.roteiro_service.service.SugestaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roteiros")
public class RoteiroController {

    private final RoteiroService roteiroService;
    private final SugestaoService sugestaoService;

    public RoteiroController(RoteiroService roteiroService, SugestaoService sugestaoService) {
        this.roteiroService = roteiroService;
        this.sugestaoService = sugestaoService;
    }

    @PostMapping
    public Roteiro criarRoteiro(@RequestBody Roteiro roteiro) {
        return roteiroService.save(roteiro);
    }

    @GetMapping
    public List<Roteiro> listarRoteiros() {
        return roteiroService.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Roteiro> atualizarRoteiro(@PathVariable Long id, @RequestBody Roteiro roteiroDetails) {
        try {
            Roteiro updatedRoteiro = roteiroService.update(id, roteiroDetails);
            return ResponseEntity.ok(updatedRoteiro);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRoteiro(@PathVariable Long id) {
        try {
            roteiroService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Novo endpoint para obter sugestões
    @PostMapping("/sugestao")
    public SuggestionResponse obterSugestaoDeRoteiro(@RequestBody SuggestionRequest request) {
        return sugestaoService.obterSugestao(request.getCountry());
    }
}
