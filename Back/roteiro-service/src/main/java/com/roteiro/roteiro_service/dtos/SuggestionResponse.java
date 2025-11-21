package com.roteiro.roteiro_service.dtos;

import java.util.List;

public class SuggestionResponse {
    private Integer days;
    private List<String> places;
    private String averageCost;
    private String description;

    public SuggestionResponse() {}

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public List<String> getPlaces() {
        return places;
    }

    public void setPlaces(List<String> places) {
        this.places = places;
    }

    public String getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(String averageCost) {
        this.averageCost = averageCost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
