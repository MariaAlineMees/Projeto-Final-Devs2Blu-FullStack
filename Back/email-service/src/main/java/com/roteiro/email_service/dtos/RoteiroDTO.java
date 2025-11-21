package com.roteiro.email_service.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoteiroDTO implements Serializable {
    private Long id;
    private String titulo;
    private String destino;
    private String dataInicio;
    private String dataFim;
    private double custoEstimado;
    private String userEmail;
    private String username; // Adicionado nome do usuário
}
