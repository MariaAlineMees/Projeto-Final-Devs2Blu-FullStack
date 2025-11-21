package com.roteiro.email_service.consumer;

import com.roteiro.email_service.config.RabbitMQConfig;
import com.roteiro.email_service.dtos.RoteiroDTO; // Importar o DTO
import com.roteiro.email_service.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailMessageConsumer {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.ROTEIRO_CRIADO_QUEUE)
    public void receiveRoteiroCriadoMessage(RoteiroDTO roteiroDTO) { // Alterado para receber RoteiroDTO
        System.out.println("Received RoteiroDTO from roteiro.criado.queue: " + roteiroDTO);
        // Chamar o serviço de e-mail para enviar a confirmação
        emailService.sendRoteiroConfirmationEmail(roteiroDTO);
    }
}
