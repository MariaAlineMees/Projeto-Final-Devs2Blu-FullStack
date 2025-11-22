package com.roteiro.roteiro_service.controller;

import com.roteiro.roteiro_service.dtos.UserDTO; // Importar UserDTO
import com.roteiro.roteiro_service.model.User;
import com.roteiro.roteiro_service.service.AuthService;
import org.springframework.amqp.rabbit.core.RabbitTemplate; // Importar RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired; // Importar Autowired
import org.springframework.beans.factory.annotation.Value; // Importar Value
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.queue.user.registered}")
    private String userRegisteredQueue;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = authService.register(user);

            // --- ENVIAR MENSAGEM PARA A FILA ---
            UserDTO userDTO = new UserDTO(registeredUser.getUsername(), registeredUser.getEmail());
            rabbitTemplate.convertAndSend(userRegisteredQueue, userDTO);


            // Não retorne a senha no response
            registeredUser.setPassword(null); 
            return ResponseEntity.ok(registeredUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser(Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok(principal.getName());
        }
        return ResponseEntity.status(401).build();
    }
}
