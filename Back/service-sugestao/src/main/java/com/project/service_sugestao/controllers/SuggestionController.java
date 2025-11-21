package com.project.service_sugestao.controllers;

import com.project.service_sugestao.dtos.SuggestionRequest;
import com.project.service_sugestao.dtos.SuggestionResponse;
import com.project.service_sugestao.services.OllamaService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/ai")
public class SuggestionController {

    private final OllamaService ollamaService;

    public SuggestionController(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    @PostMapping(value = "/suggestion", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<SuggestionResponse> suggestion(@RequestBody SuggestionRequest request) {
        String country = request.getCountry() != null ? request.getCountry().trim() : "";

        if (country.isEmpty()) {
            SuggestionResponse empty = new SuggestionResponse();
            empty.setDescription("Informe um país no campo 'country'.");
            return Mono.just(empty);
        }

        return ollamaService.generateSuggestion(country);
    }
}