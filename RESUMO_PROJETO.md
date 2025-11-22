# Guia de Apresentação do Projeto: Meu Roteiro 🌍

Este documento serve como um roteiro para explicar o projeto, sua arquitetura, tecnologias e os desafios enfrentados durante o desenvolvimento.

---

### 1. Qual é a ideia do projeto?

**Resposta:** O projeto é o **"Meu Roteiro"**, uma aplicação web full-stack que permite a um usuário criar, visualizar, editar e deletar seus próprios roteiros de viagem.

O objetivo é resolver um problema de organização: em vez de ter informações de viagem espalhadas em planilhas ou blocos de notas, o usuário pode centralizar tudo em uma interface simples, segura e personalizada.

---

### 2. Como o sistema foi construído? Qual a arquitetura?

**Resposta:** O sistema utiliza uma arquitetura moderna baseada em **microsserviços**, totalmente **containerizada com Docker**. A aplicação é dividida em três partes principais:

*   **Front-end (A Interface do Usuário):**
    *   **Tecnologia:** **Angular**.
    *   **Função:** É a tela que o usuário vê no navegador. Ele é responsável por exibir as páginas de login, registro, a nova tela de boas-vindas, o formulário de criação de roteiros e a lista de roteiros. Sua única função é se comunicar com o back-end através de uma API REST.

*   **Back-end (O Cérebro do Sistema):**
    *   O back-end é dividido em **dois microsserviços Spring Boot**.
    *   **1. `roteiro-service` (O Serviço Principal):**
        *   **Tecnologia:** Spring Boot com Java 17 e **Spring Security**.
        *   **Função:** Expõe a API REST principal, gerencia o CRUD de roteiros e controla toda a segurança da API, incluindo o processamento de registro, login e autorização de usuários.
        *   **Papel na Mensageria:** Atua como **Produtor**.
    *   **2. `email-service` (O Serviço de Notificação):**
        *   **Tecnologia:** Spring Boot com Java 17 e Spring Mail.
        *   **Função:** Sua única responsabilidade é "ouvir" mensagens de eventos (como registro de usuário e criação de roteiro) e enviar e-mails de notificação para o usuário.
        *   **Papel na Mensageria:** Atua como **Consumidor**.

*   **Infraestrutura (A Base de Tudo):**
    *   **Banco de Dados:** **MySQL**, rodando em um contêiner Docker para guardar os dados de usuários e roteiros de forma permanente.
    *   **Mensageria:** **RabbitMQ**, também em um contêiner, servindo como um "carteiro" para garantir a comunicação assíncrona entre os serviços de back-end.

---

### 3. Como a Segurança foi Implementada?

**Resposta:** A segurança foi um pilar central do projeto, garantindo que os dados de cada usuário sejam privados e seguros.

-   **Autenticação:** Utiliza o **Spring Security** com `formLogin` baseado em sessão. As senhas dos usuários são criptografadas com **BCrypt** antes de serem salvas no banco de dados.
-   **Autorização:** Após o login, um usuário **só pode ver e gerenciar os seus próprios roteiros**. Toda a lógica no back-end foi alterada para sempre filtrar os dados pelo usuário autenticado na sessão.
-   **Proteção de Rotas:** No front-end, as rotas são protegidas por um **AuthGuard** do Angular, que redireciona qualquer usuário não logado para a página de login.

---

### 4. Como funciona a mensageria com RabbitMQ neste projeto?

**Resposta:** O projeto implementa dois fluxos de negócio com comunicação assíncrona:

1.  **E-mail de Boas-Vindas:** Ao se registrar, o `roteiro-service` publica uma mensagem na fila `user.registered.queue`, e o `email-service` a consome para enviar um e-mail de boas-vindas.
2.  **E-mail de Confirmação de Roteiro:** Ao criar um roteiro, o `roteiro-service` publica uma mensagem na fila `roteiro.criado.queue`, e o `email-service` a consome para enviar um e-mail de confirmação.

A vantagem é que a resposta para o usuário é imediata, sem que ele precise esperar pelo envio do e-mail.

---

### 5. Quais foram os principais desafios e aprendizados?

**Resposta:** Além de construir a estrutura inicial, passamos por um processo de depuração e refinamento muito importante:

1.  **Containerização Completa:** Criamos `Dockerfile` para cada serviço (incluindo builds de múltiplos estágios para o backend) e um `docker-compose.yml` na raiz para orquestrar tudo com um único comando.

2.  **Persistência de Dados:** Configuramos um **volume** no Docker para o MySQL, garantindo que os dados não sejam perdidos.

3.  **Refatoração do Front-end:** A interface inicial foi completamente refatorada para uma experiência mais moderna e intuitiva:
    *   Criação de uma tela de boas-vindas (`/home`).
    *   Separação da funcionalidade de "Criar Roteiro" e "Listar Roteiros" em componentes e rotas distintas.
    *   Implementação de uma barra de navegação funcional para alternar entre as telas.

4.  **Resolução de Erros de Rede e CORS (O Desafio Final):** O maior desafio técnico foi a comunicação entre o front-end e o back-end. A solução definitiva foi implementar uma arquitetura padrão de produção:
    *   **Proxy Reverso:** Configuramos o **Nginx** para atuar como um **proxy reverso**. Agora, o front-end envia as requisições para si mesmo (em `/api/...`), e o Nginx redireciona essa chamada para o `roteiro-service` dentro da rede segura do Docker, **garantindo o repasse correto dos cookies de sessão**, o que é fundamental para a autenticação.

Hoje, o sistema está 100% funcional, seguro, com o fluxo completo rodando de forma estável e integrada.
