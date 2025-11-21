package com.roteiro.email_service.consumer;

import com.roteiro.email_service.config.RabbitMQConfig;
import com.roteiro.email_service.dtos.RoteiroDTO;
import com.roteiro.email_service.dtos.UserDTO; // Importar UserDTO
import com.roteiro.email_service.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailMessageConsumer {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.ROTEIRO_CRIADO_QUEUE)
    public void receiveRoteiroCriadoMessage(RoteiroDTO roteiroDTO) {
        System.out.println("Received RoteiroDTO from roteiro.criado.queue: " + roteiroDTO);
        emailService.sendRoteiroConfirmationEmail(roteiroDTO);
    }

    // --- NOVO MÉTODO PARA OUVIR A FILA DE REGISTRO ---
    @RabbitListener(queues = "${rabbitmq.queue.user.registered}")
    public void receiveUserRegisteredMessage(UserDTO userDTO) {
        System.out.println("Received UserDTO from user.registered.queue: " + userDTO);
        emailService.sendWelcomeEmail(userDTO);
    }
    // --- FIM DO NOVO MÉTODO ---
}
