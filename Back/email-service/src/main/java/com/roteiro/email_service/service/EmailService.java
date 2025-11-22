package com.roteiro.email_service.service;

import com.roteiro.email_service.dtos.RoteiroDTO;
import com.roteiro.email_service.dtos.UserDTO; // Importar UserDTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.Locale;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRoteiroConfirmationEmail(RoteiroDTO roteiroDTO) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(roteiroDTO.getUserEmail());
        message.setFrom("suporte.roteiroviagens@gmail.com");
        message.setSubject("Confirmação de Criação de Roteiro: " + roteiroDTO.getTitulo());

        Locale ptBr = new Locale("pt", "BR");
        NumberFormat formatadorMoeda = NumberFormat.getCurrencyInstance(ptBr);
        String custoFormatado = formatadorMoeda.format(roteiroDTO.getCustoEstimado());

        String textoEmail = String.format(
            "Olá, viajante!\n\n" +
            "Seu roteiro '%s' para o destino '%s' com início em %s e fim em %s foi criado com sucesso.\n\n" +
            "Custo estimado: %s\n\n" +
            "Atenciosamente,\nSua Equipe de Roteiros",
            roteiroDTO.getTitulo(),
            roteiroDTO.getDestino(),
            roteiroDTO.getDataInicio(),
            roteiroDTO.getDataFim(),
            custoFormatado
        );

        message.setText(textoEmail);
        mailSender.send(message);
        System.out.println("E-mail de confirmação de roteiro enviado para: " + roteiroDTO.getUserEmail());
    }

    // --- MÉTODO PARA E-MAIL DE BOAS-VINDAS ---
    public void sendWelcomeEmail(UserDTO userDTO) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(userDTO.getEmail());
        message.setFrom("suporte.roteiroviagens@gmail.com");
        message.setSubject("Bem-vindo ao Roteiro de Viajens!");

        String textoEmail = String.format(
            "Olá, %s!\n\n" +
            "Seja bem-vindo(a) ao nosso aplicativo de roteiros de viagem.\n\n" +
            "Estamos felizes em ter você conosco. Comece a planejar sua próxima aventura agora mesmo!\n\n" +
            "Atenciosamente,\nSua Equipe de Roteiros",
            userDTO.getUsername()
        );

        message.setText(textoEmail);
        mailSender.send(message);
        System.out.println("E-mail de boas-vindas enviado para: " + userDTO.getEmail());
    }

}
