# Roteiro de Apresentação: Meu Roteiro 🌍

**Objetivo:** Apresentar o projeto full-stack "Meu Roteiro", demonstrando suas funcionalidades, arquitetura e tecnologias, em um vídeo de 5 a 7 minutos.

---

### **Parte 1: Introdução e Visão Geral (Aprox. 1 minuto)**

***(Cena: Comece com a aplicação rodando no navegador, na tela de login.)***

"Bom dia! Meu nome é Maria Aline, e este é o meu projeto final, o **'Meu Roteiro'**.

A ideia do projeto é resolver um problema comum para quem gosta de viajar: a falta de um lugar centralizado para organizar os planos. Em vez de usar planilhas ou blocos de notas, o 'Meu Roteiro' permite que o usuário crie, gerencie e visualize seus roteiros de viagem de forma simples, segura e personalizada.

O sistema foi construído com uma arquitetura moderna de microsserviços, totalmente containerizada com Docker, usando Angular no frontend e Spring Boot com Java no backend."

---

### **Parte 2: Demonstração do Sistema (Aprox. 3 minutos)**

***(Cena: Navegue pela aplicação enquanto descreve as funcionalidades.)***

"Vamos começar a demonstração.

**1. Registro e Login:** Primeiro, um novo usuário pode se registrar na plataforma. *(Mostre a tela de registro, preencha e registre um novo usuário)*.

***(Cena: Mude rapidamente para a sua caixa de entrada do e-mail e mostre o e-mail de 'Boas-Vindas' chegando.)***

**"E, como podem ver, o `email-service` já consumiu a mensagem do RabbitMQ e me enviou um e-mail de boas-vindas. Isso acontece de forma assíncrona, sem travar a experiência do usuário."**

*(Volte para a aplicação)*. "Agora, vamos fazer o login com o novo usuário." *(Faça o login)*.

**2. Tela de Boas-Vindas:** Após o login, somos recebidos por uma tela de boas-vindas limpa e personalizada. A barra de navegação agora mostra as opções para o usuário logado.

**3. Criação de Roteiro:** Vamos criar um novo roteiro. Clicando em 'Novo Roteiro', somos levados ao formulário. *(Preencha o formulário para um novo roteiro, ex: "Fim de Semana em Gramado")*. Ao salvar, recebemos uma notificação de sucesso...

***(Cena: Mude rapidamente para a sua caixa de entrada do e-mail e mostre o e-mail de 'Confirmação de Roteiro' chegando.)***

**"...e, ao mesmo tempo, o `email-service` recebeu o evento e enviou um e-mail confirmando a criação do nosso roteiro."**

*(Volte para a aplicação)*. "...e somos redirecionados para a lista de roteiros, onde nosso novo plano já aparece."

**4. Listagem e Segurança:** Na tela 'Meus Roteiros', podemos ver o roteiro que acabamos de criar. Um ponto crucial da segurança é que **um usuário só pode ver os seus próprios roteiros**. Se eu fizer logout e entrar com outra conta, os roteiros da primeira conta não aparecerão aqui.

**5. Edição e Exclusão:** Podemos também editar um roteiro existente. *(Clique em 'EDITAR', altere um campo como o custo e clique em 'Atualizar')*. A informação é atualizada na lista. E, claro, podemos deletar um roteiro. *(Clique em 'DELETAR' e confirme)*.

Toda a interface foi projetada para ser responsiva, se adaptando a diferentes tamanhos de tela, como celulares e tablets."

---

### **Parte 3: Arquitetura e Tecnologias (Aprox. 1.5 minutos)**

***(Cena: Mude a tela para o `docker-compose.yml` ou para o `README.md`.)***

"Por trás dessa interface, temos uma arquitetura de microsserviços orquestrada com Docker Compose.

*   **`roteiro-front`:** É a aplicação Angular que acabamos de ver, servida por um Nginx que também atua como Proxy Reverso.
*   **`roteiro-service`:** É o cérebro da aplicação. Um microsserviço Spring Boot que gerencia toda a lógica de roteiros e a segurança com **Spring Security**.
*   **`email-service`:** Um segundo microsserviço Spring Boot, responsável apenas por enviar notificações.
*   **`mysql-db` e `rabbitmq`:** O banco de dados e o broker de mensagens, ambos rodando em seus próprios contêineres.

O projeto inteiro sobe com um único comando, `docker compose up --build`, o que demonstra a portabilidade do ambiente."

---

### **Parte 4: Fluxo de Mensageria com RabbitMQ (Aprox. 1 minuto)**

***(Cena: Mantenha a visão no `docker-compose.yml` ou no `README.md`.)***

"Um requisito chave do projeto era o uso de mensageria para comunicação assíncrona, e a demonstração dos e-mails é o resultado final desse fluxo.

1.  **No registro de um novo usuário:** O `roteiro-service` publica uma mensagem na fila `user.registered.queue`.
2.  **Na criação de um novo roteiro:** Ele publica uma mensagem na fila `roteiro.criado.queue`.

Em ambos os casos, o `email-service` consome essas mensagens e dispara os e-mails que vimos. Isso torna a aplicação mais robusta e a experiência do usuário mais rápida.

Este foi o projeto 'Meu Roteiro'. Obrigado!"
