package com.roteiro.email_service.service;

import com.roteiro.email_service.dtos.RoteiroDTO; // Importar o DTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRoteiroConfirmationEmail(RoteiroDTO roteiroDTO) { // Alterado para receber RoteiroDTO
        SimpleMailMessage message = new SimpleMailMessage();

        // --- MUDANÇA: Usar o e-mail do DTO ---
        message.setTo(roteiroDTO.getUserEmail());

        // --- CORREÇÃO: Usar um endereço de e-mail válido como remetente ---
        message.setFrom("noreply@roteiroapp.com");

        message.setSubject("Confirmação de Criação de Roteiro: " + roteiroDTO.getTitulo());
        message.setText(
            "Olá!\n\nSeu roteiro '" + roteiroDTO.getTitulo() + 
            "' para o destino '" + roteiroDTO.getDestino() + 
            "' com início em " + roteiroDTO.getDataInicio() + 
            " e fim em " + roteiroDTO.getDataFim() + 
            " foi criado com sucesso.\n\n" +
            "Custo estimado: " + String.format("%.2f", roteiroDTO.getCustoEstimado()) + 
            "\n\nAtenciosamente,\nSua Equipe de Roteiros"
        );
        mailSender.send(message);
        System.out.println("E-mail de confirmação enviado para: " + roteiroDTO.getUserEmail() + " para o roteiro: " + roteiroDTO.getTitulo());
    }
}
