> **Nota:** Este documento descreve os componentes do Back-end. Para uma visão geral do projeto completo e instruções de como rodar a aplicação, por favor, consulte o **[README.md principal](../README.md)**.

---

## ✈ Back-end: Planejador de Roteiros de Viagem

Este diretório contém os microsserviços desenvolvidos em **Spring Boot** que formam o back-end da aplicação.

### 1. Arquitetura do Back-end

O back-end é composto por dois microsserviços:

1.  **`roteiro-service` (Produtor):**
    *   **Responsabilidades:**
        *   Gerencia o CRUD (Criar, Ler, Atualizar, Deletar) da entidade `Roteiro`.
        *   Gerencia o registro e a autenticação de usuários (`User`).
        *   Implementa a camada de segurança com **Spring Security** para proteger os endpoints.
    *   **Tecnologias:** Spring Boot, Spring Data JPA, Spring Security, Spring AMQP.
    *   **Comunicação:** Expõe uma API REST para o front-end e, ao criar um novo roteiro ou registrar um usuário, **produz** mensagens para as filas correspondentes no RabbitMQ.

2.  **`email-service` (Consumidor):**
    *   **Responsabilidade:** Ouve as filas de eventos (`roteiro.criado.queue` e `user.registered.queue`) e envia e-mails de notificação (confirmação de roteiro e boas-vindas) usando um provedor SMTP (como SendGrid).
    *   **Tecnologias:** Spring Boot, Spring AMQP (RabbitMQ), Spring Mail.
    *   **Comunicação:** Consome mensagens das filas do RabbitMQ.

---

### 2. Segurança com Spring Security

A API do `roteiro-service` é protegida para garantir que apenas usuários autenticados possam gerenciar seus roteiros.

-   **Autenticação:** Utiliza um sistema de `formLogin` baseado em sessão. As credenciais dos usuários são armazenadas de forma segura no banco de dados com senhas criptografadas usando `BCryptPasswordEncoder`.
-   **Autorização:** Cada roteiro é associado a um usuário. As regras de negócio garantem que um usuário só pode visualizar, atualizar ou deletar os roteiros que ele mesmo criou.

#### Endpoints de API

-   `POST /api/login` e `POST /api/logout`: Endpoints gerenciados pelo Spring Security para login e logout.
-   `POST /api/auth/register`: Endpoint **público** para registrar um novo usuário.
-   `GET /api/auth/me`: Endpoint **protegido** que retorna o nome de usuário da sessão atualmente logada.

---

### 3. Fluxos de Mensageria (RabbitMQ)

O projeto implementa dois fluxos de negócio com comunicação assíncrona:

1.  **E-mail de Boas-Vindas:**
    *   **Ação:** Um novo usuário se registra através da API do `roteiro-service`.
    *   **Produtor (`roteiro-service`):** Após salvar o usuário, produz uma mensagem com os dados do usuário (sem a senha) para a fila `user.registered.queue`.
    *   **Consumidor (`email-service`):** Consome a mensagem e envia um e-mail de boas-vindas para o endereço de e-mail do novo usuário.

2.  **E-mail de Confirmação de Roteiro:**
    *   **Ação:** Um usuário autenticado cria um novo roteiro.
    *   **Produtor (`roteiro-service`):** Após salvar o roteiro, produz uma mensagem com os detalhes do roteiro e o e-mail do usuário para a fila `roteiro.criado.queue`.
    *   **Consumidor (`email-service`):** Consome a mensagem e envia um e-mail de confirmação para o usuário.
