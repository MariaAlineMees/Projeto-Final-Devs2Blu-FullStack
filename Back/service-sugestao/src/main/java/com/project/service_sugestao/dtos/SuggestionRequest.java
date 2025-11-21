package com.project.service_sugestao.dtos;

public class SuggestionRequest {
    private String country;

    public SuggestionRequest() {}

    public SuggestionRequest(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
