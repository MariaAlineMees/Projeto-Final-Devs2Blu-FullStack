> **Nota:** Este documento descreve os componentes do Back-end. Para uma visão geral do projeto completo e instruções de como rodar a aplicação, por favor, consulte o **[README.md principal](../README.md)**.

---

## ✈ Back-end: Planejador de Roteiros de Viagem

Este diretório contém os microsserviços desenvolvidos em **Spring Boot** que formam o back-end da aplicação.

### 1. Arquitetura do Back-end

O back-end é composto por dois microsserviços:

1.  **`roteiro-service` (Produtor):**
    *   **Responsabilidade:** Gerencia o CRUD (Criar, Ler, Atualizar, Deletar) da entidade `Roteiro`.
    *   **Tecnologias:** Spring Boot, Spring Data JPA.
    *   **Comunicação:** Expõe uma API REST para o front-end e, ao criar um novo roteiro, produz uma mensagem para o RabbitMQ.

2.  **`sugestao-service` (Consumidor):**
    *   **Responsabilidade:** Ouve a fila de roteiros criados e simula um processamento assíncrono (como a geração de sugestões).
    *   **Tecnologias:** Spring Boot, Spring AMQP (RabbitMQ).
    *   **Comunicação:** Consome mensagens da fila `roteiro.criado.queue`.

### 2. Fluxo de Mensageria (RabbitMQ)

O fluxo de negócio implementa a comunicação assíncrona, cumprindo a exigência de haver pelo menos 1 fluxo de negócio real passando pela fila.

1.  **Ação (API):** Um novo Roteiro é criado via requisição da API para o `roteiro-service`.
2.  **Produtor (`roteiro-service`):** Após salvar o roteiro no MySQL, o serviço **produz** uma mensagem para a fila.
3.  **Consumidor (`sugestao-service`):** O serviço **consome** a mensagem da fila e imprime os dados recebidos no console para confirmar o recebimento e processamento.

### 3. Tecnologias Utilizadas

| Componente        | Tecnologia            |
| :---------------- | :-------------------- |
| *Framework Core*  | Spring Boot 3 + Java 17 |
| *Banco de Dados*  | MySQL 8.0             |
| *Mensageria*      | RabbitMQ              |
| *Containerização* | Docker                |
