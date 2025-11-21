package com.roteiro.email_service.service;

import com.roteiro.email_service.model.Roteiro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRoteiroConfirmationEmail(Roteiro roteiro) {
        SimpleMailMessage message = new SimpleMailMessage();
        
        // --- ATENÇÃO: PREENCHA COM OS DADOS CORRETOS ---
        // 1. E-mail do DESTINATÁRIO: Pode ser um e-mail de teste (ex: "test@example.com")
        //    ou o e-mail real do usuário se o objeto Roteiro contiver essa informação.
        message.setTo("mariaalinemees@gmail.com");

        // 2. E-mail do REMETENTE: Use o "Username" do Mailtrap (ex: "seu_username_do_mailtrap@mailtrap.io")
        //    ou um e-mail genérico como "noreply@seusistema.com".
        //    Este e-mail deve ser o mesmo configurado em spring.mail.username no application.properties.
        message.setFrom("sandbox.smtp.mailtrap.io");
        // --- FIM DA ATENÇÃO ---

        message.setSubject("Confirmação de Criação de Roteiro: " + roteiro.getTitulo());
        message.setText(
            "Olá!\n\nSeu roteiro '" + roteiro.getTitulo() + 
            "' para o destino '" + roteiro.getDestino() + 
            "' com início em " + roteiro.getDataInicio() + 
            " e fim em " + roteiro.getDataFim() + 
            " foi criado com sucesso.\n\n" +
            "Custo estimado: " + String.format("%.2f", roteiro.getCustoEstimado()) + 
            "\n\nAtenciosamente,\nSua Equipe de Roteiros"
        );
        mailSender.send(message);
        System.out.println("E-mail de confirmação enviado para: " + message.getTo()[0] + " para o roteiro: " + roteiro.getTitulo());
    }
}
