# Guia de Apresentação do Projeto: Planejador de Roteiros de Viagem

Este documento serve como um roteiro para explicar o projeto, sua arquitetura, tecnologias e os desafios enfrentados durante o desenvolvimento.

---

### 1. Qual é a ideia do projeto?

**Resposta:** O projeto é um **Planejador de Roteiros de Viagem**, uma aplicação web full-stack que permite a um usuário criar, visualizar, editar e deletar seus próprios roteiros de viagem.

O objetivo é resolver um problema de organização: em vez de ter informações de viagem espalhadas em planilhas ou blocos de notas, o usuário pode centralizar tudo em uma interface simples, segura e personalizada.

---

### 2. Como o sistema foi construído? Qual a arquitetura?

**Resposta:** O sistema utiliza uma arquitetura moderna baseada em **microsserviços**, totalmente **containerizada com Docker**. A aplicação é dividida em três partes principais:

*   **Front-end (A Interface do Usuário):**
    *   **Tecnologia:** **Angular**.
    *   **Função:** É a tela que o usuário vê no navegador. Ele é responsável por exibir as páginas de login, registro, o formulário de criação de roteiros e a lista de roteiros. Sua única função é se comunicar com o back-end através de uma API REST.

*   **Back-end (O Cérebro do Sistema):**
    *   O back-end é dividido em **dois microsserviços Spring Boot**.
    *   **1. `roteiro-service` (O Serviço Principal):**
        *   **Tecnologia:** Spring Boot com Java 17 e **Spring Security**.
        *   **Função:** Expõe a API REST principal, gerencia o CRUD de roteiros e, o mais importante, **controla toda a segurança da aplicação**, incluindo registro, login e autorização de usuários.
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

**Resposta:** A segurança foi um pilar central do projeto, garantindo que os dados de cada usuário sejam privados e seguros. Implementamos isso em duas camadas:

*   **Autenticação (Quem é você?):**
    *   Utilizamos o **Spring Security** para criar um sistema de login completo.
    *   O usuário primeiro precisa se **registrar**, e seus dados (usuário e senha) são salvos no banco de dados.
    *   Um ponto crucial é que a senha **não é salva em texto puro**. Ela é **criptografada** com o algoritmo **BCrypt**, uma prática padrão de mercado que torna impossível reverter a senha.
    *   Ao fazer login, o Spring Security compara a senha digitada com a versão criptografada no banco e, se tudo estiver correto, cria uma sessão segura para o usuário.

*   **Autorização (O que você pode fazer?):**
    *   Este foi o passo mais importante. Após o login, um usuário **só pode ver e gerenciar os seus próprios roteiros**.
    *   Para isso, ligamos a tabela de `roteiros` à tabela de `users` no banco de dados. Cada roteiro agora tem um "dono".
    *   Toda a lógica no back-end foi alterada para sempre filtrar os dados pelo usuário que está logado na sessão. Assim, a "Aline" nunca conseguirá ver os roteiros do "João", e vice-versa.
    *   No front-end, a rota `/roteiros` é protegida por um **AuthGuard** do Angular, que redireciona qualquer usuário não logado para a página de login.

---

### 4. Como funciona a mensageria com RabbitMQ neste projeto?

**Resposta:** O projeto implementa dois fluxos de negócio com comunicação assíncrona:

1.  **E-mail de Boas-Vindas no Registro:**
    *   **Ação:** Um novo usuário se registra através da interface.
    *   **Produção:** O `roteiro-service` salva o usuário no MySQL e **publica uma mensagem** com os dados do usuário para a fila `user.registered.queue`.
    *   **Consumo:** O `email-service` consome a mensagem e **envia um e-mail de boas-vindas** para o endereço de e-mail do novo usuário.

2.  **E-mail de Confirmação de Roteiro:**
    *   **Ação:** Um usuário autenticado cria um novo roteiro.
    *   **Produção:** O `roteiro-service` salva o roteiro no MySQL e **publica uma mensagem** com os detalhes do roteiro para a fila `roteiro.criado.queue`.
    *   **Consumo:** O `email-service` consome a mensagem e **envia um e-mail de confirmação** para o usuário.

A vantagem é que a resposta para o usuário é imediata, sem que ele precise esperar pelo envio do e-mail.

---

### 5. Quais foram os principais desafios e aprendizados?

**Resposta:** Além de construir a estrutura inicial, passamos por um processo de depuração e refinamento muito importante:

1.  **Containerização Completa:** Criamos `Dockerfile` para cada serviço e um `docker-compose.yml` na raiz para orquestrar tudo com um único comando.

2.  **Persistência de Dados:** Configuramos um **volume** no Docker para o MySQL, garantindo que os dados não sejam perdidos.

3.  **Implementação de Autenticação e Autorização:** Um desafio crucial foi transformar a aplicação de um sistema de "usuário único" para um sistema **multiusuário real**. Isso envolveu:
    *   Criar as entidades `User` e `Roteiro` e ligá-las no banco de dados.
    *   Configurar o Spring Security para usar o banco de dados em vez de um usuário em memória.
    *   Modificar **toda a lógica de negócio** no back-end para garantir que um usuário só pudesse ver e modificar seus próprios roteiros.
    *   Criar os componentes de Login e Registro no front-end e um serviço (`AuthService`) para gerenciar o estado de autenticação.

4.  **Implementação de Notificações Assíncronas:** Um dos principais requisitos foi a implementação de um fluxo de negócio real com mensageria. Isso foi alcançado com a criação do `email-service`, que permitiu:
    *   Enviar e-mails de boas-vindas e de confirmação de forma desacoplada e assíncrona.
    *   Integrar a aplicação com um serviço de SMTP real (SendGrid), configurando credenciais e remetentes verificados.
    *   Garantir que a experiência do usuário seja rápida, pois ele não precisa esperar o envio do e-mail para continuar usando a aplicação.

5.  **Resolução de Erros de Rede e CORS (O Desafio Final):** O maior desafio técnico foi a comunicação entre o front-end e o back-end. A solução definitiva foi implementar uma arquitetura padrão de produção:
    *   **Proxy Reverso:** Configuramos o **Nginx** para atuar como um **proxy reverso**. Agora, o front-end envia as requisições para si mesmo (em `/api/...`), e o Nginx redireciona essa chamada para o `roteiro-service` dentro da rede segura do Docker. Isso eliminou todos os erros de comunicação.

Hoje, o sistema está 100% funcional, seguro, com o fluxo completo rodando de forma estável e integrada.
