package com.project.service_sugestao.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.service_sugestao.dtos.SuggestionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class OllamaService {

    private final WebClient webClient;
    private final String model;
    private final ObjectMapper mapper = new ObjectMapper();

    public OllamaService(WebClient.Builder webClientBuilder,
                         @Value("${spring.ai.ollama.base-url}") String baseUrl,
                         @Value("${spring.ai.ollama.chat.options.model}") String model) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.model = model;
    }

    public Mono<SuggestionResponse> generateSuggestion(String country) {
        String prompt = String.format(
                "Gere uma sugestão de viagem para o país \"%s\". "
                        + "Retorne SOMENTE um JSON com os campos: \n"
                        + "days, places, average_cost, description.",
                country
        );

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("prompt", prompt);
        body.put("stream", false); // Desabilitar streaming para obter a resposta completa
        body.put("format", "json"); // Solicitar a saída em formato JSON

        return webClient.post()
                .uri("/api/generate") // Usar o endpoint correto da API Ollama
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .flatMap(respMap -> {
                    // A resposta da API Ollama vem em um campo "response" como uma string JSON
                    String jsonResponse = respMap.get("response").toString();

                    try {
                        JsonNode root = mapper.readTree(jsonResponse);
                        SuggestionResponse suggestion = new SuggestionResponse();

                        if (root.has("days")) suggestion.setDays(root.get("days").asInt());

                        if (root.has("places") && root.get("places").isArray()) {
                            suggestion.setPlaces(
                                    mapper.convertValue(
                                            root.get("places"),
                                            mapper.getTypeFactory().constructCollectionType(java.util.List.class, String.class)
                                    )
                            );
                        }

                        if (root.has("average_cost"))
                            suggestion.setAverageCost(root.get("average_cost").asText());

                        if (root.has("description"))
                            suggestion.setDescription(root.get("description").asText());

                        return Mono.just(suggestion);

                    } catch (Exception e) {
                        // Se a análise falhar, retorne a resposta bruta para depuração
                        SuggestionResponse fallback = new SuggestionResponse();
                        fallback.setDescription(jsonResponse);
                        return Mono.just(fallback);
                    }
                });
    }
}
