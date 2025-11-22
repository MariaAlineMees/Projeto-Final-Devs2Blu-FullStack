package com.roteiro.roteiro_service.service;

import com.roteiro.roteiro_service.dtos.SuggestionRequest;
import com.roteiro.roteiro_service.dtos.SuggestionResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SugestaoService {

    private final RestTemplate restTemplate;

    public SugestaoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SuggestionResponse obterSugestao(String pais) {
        String url = "http://localhost:8082/api/ai/suggestion";
        
        SuggestionRequest request = new SuggestionRequest(pais);

        // Fazer a chamada HTTP POST síncrona, enviando SuggestionRequest e esperando SuggestionResponse
        return restTemplate.postForObject(url, request, SuggestionResponse.class);
    }
}
