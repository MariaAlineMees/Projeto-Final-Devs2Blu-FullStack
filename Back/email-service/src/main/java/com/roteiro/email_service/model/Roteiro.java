package com.roteiro.email_service.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Roteiro {
    private Long id;
    private String titulo;
    private String destino;
    private String dataInicio;
    private String dataFim;
    private double custoEstimado;
}
