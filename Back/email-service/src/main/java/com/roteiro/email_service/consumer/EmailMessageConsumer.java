package com.roteiro.email_service.consumer;

import com.roteiro.email_service.config.RabbitMQConfig;
import com.roteiro.email_service.model.Roteiro;
import com.roteiro.email_service.service.EmailService; // Importar o EmailService
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailMessageConsumer {

    @Autowired
    private EmailService emailService; // Injetar o EmailService

    @RabbitListener(queues = RabbitMQConfig.ROTEIRO_CRIADO_QUEUE)
    public void receiveRoteiroCriadoMessage(Roteiro roteiro) {
        System.out.println("Received Roteiro from roteiro.criado.queue: " + roteiro);
        // Chamar o serviço de e-mail para enviar a confirmação
        emailService.sendRoteiroConfirmationEmail(roteiro);
    }
}
