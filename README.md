# ✈️ Projeto Fullstack: Planejador de Roteiros de Viagem

Este é o projeto final da disciplina, um sistema fullstack completo para gerenciar a criação e o processamento de roteiros de viagem pessoais. A aplicação utiliza uma arquitetura de microsserviços com Spring Boot, Angular, MySQL e RabbitMQ, totalmente containerizada com Docker.

| Item                | Detalhe                                                              |
| :------------------ | :------------------------------------------------------------------- |
| **Nome do Projeto** | Planejador de Roteiros de Viagem                                     |
| **Integrantes**     | Aline                                                                |
| **Descrição**       | Uma plataforma para usuários criarem, gerenciarem e acompanharem seus planos de viagem, com um sistema de notificações assíncrono. |
| **Público Alvo**    | Viajantes que desejam organizar suas futuras aventuras de forma simples e centralizada. |

---

### 2. Tecnologias Utilizadas

| Camada      | Tecnologia            | Descrição                                                              |
| :---------- | :-------------------- | :--------------------------------------------------------------------- |
| **Front-end** | Angular               | Interface de usuário reativa para interagir com a API.                 |
| **Back-end**  | Spring Boot 3 + Java 17 | Microsserviços que expõem uma API REST (CRUD completo).                |
| **Segurança** | Spring Security       | Autenticação e autorização baseada em sessão para proteger a API.      |
| **Banco de Dados** | MySQL 8.0             | Persistência dos dados de usuários e roteiros.                         |
| **Mensageria**| RabbitMQ              | Comunicação assíncrona entre os serviços de back-end.                  |
| **Infraestrutura** | Docker & Docker Compose | Containerização e orquestração de todos os serviços da aplicação. |

---

### 3. Arquitetura e Fluxo de Dados

O sistema é composto por 3 serviços principais, um banco de dados e um broker de mensagens:

-   `roteiro-front`: A aplicação Angular que o usuário acessa no navegador.
-   `roteiro-service`: Microsserviço Spring Boot responsável pelo CRUD de roteiros e pela **autenticação/autorização de usuários**.
-   `email-service`: Microsserviço Spring Boot que "ouve" eventos (como criação de roteiro e registro de usuário) para **enviar e-mails de notificação**.

O fluxo de dados ocorre da seguinte forma:

1.  **Registro e E-mail de Boas-Vindas:**
    *   O usuário cria uma conta com e-mail através da interface.
    *   O `roteiro-service` salva o novo usuário e **produz** uma mensagem para a fila `user.registered.queue`.
    *   O `email-service` **consome** a mensagem e envia um e-mail de boas-vindas para o usuário.
2.  **Criação de Roteiro e E-mail de Confirmação:**
    *   Com o login feito, o usuário cria um novo roteiro.
    *   O `roteiro-service` salva o roteiro e **produz** uma mensagem para a fila `roteiro.criado.queue`.
    *   O `email-service` **consome** a mensagem e envia um e-mail de confirmação para o e-mail do usuário associado ao roteiro.

---

### 4. Como Rodar o Projeto Completo

Com a aplicação totalmente containerizada, o processo para rodar todo o ambiente é muito simples.

#### A. Pré-requisitos

-   Docker e Docker Compose instalados e em execução.
-   Git (para clonar o repositório).

#### B. Passo 1: Configurar a Senha do Banco de Dados

1.  Na pasta raiz do projeto, crie um arquivo chamado `.env`.
2.  Dentro do arquivo `.env`, adicione a seguinte linha, substituindo `sua_senha_segura` por uma senha de sua escolha:

    ```
    MYSQL_ROOT_PASSWORD=sua_senha_segura
    ```

#### C. Passo 2: Iniciar a Aplicação

1.  Abra um terminal na pasta raiz do projeto.
2.  Execute o seguinte comando para construir as imagens e iniciar todos os contêineres em segundo plano:

    ```sh
    docker compose up --build -d
    ```
    *A flag `--build` garante que as imagens sejam (re)construídas com as últimas alterações. Na primeira vez, o processo pode demorar alguns minutos enquanto o Maven e o NPM baixam as dependências.*

#### D. Passo 3: Utilizar a Aplicação

Após a conclusão do comando, aguarde cerca de um minuto para que todos os serviços iniciem.

1.  **Acesse a Aplicação:** Abra seu navegador e vá para `http://localhost`.
2.  **Crie uma Conta:** Você será direcionado para a página de login. Clique no link para se registrar e crie um novo usuário.
3.  **Faça Login:** Após o registro, faça login com as credenciais que você acabou de criar.
4.  **Gerencie seus Roteiros:** Agora você pode navegar entre a tela de boas-vindas, criar novos roteiros e listar os existentes usando a barra de navegação.

| Serviço             | URL de Acesso                | Portas (Host:Container) | Credenciais (se aplicável)   |
| :------------------ | :--------------------------- | :---------------------- | :---------------------------- |
| **Aplicação (Front-end)** | `http://localhost`           | `80:80`                 | Criadas pelo usuário.         |
| **API de Roteiros** | Acessada via Front-end (`/api`) | `8080:8080`             | Requer autenticação.          |
| **RabbitMQ (UI)**   | `http://localhost:15672`     | `15672:15672`           | `guest` / `guest`             |
| **Banco de Dados**  | `localhost` (via cliente SQL) | `3307:3306`             | `root` / (definida no `.env`) |

---

### 5. Para Parar a Aplicação

Para parar e remover todos os contêineres e redes, execute o seguinte comando na raiz do projeto:

```sh
docker-compose down
```
*O volume do banco de dados (`mysql-data`) não será removido por padrão, garantindo a persistência dos dados. Para remover também os dados, use `docker-compose down --volumes`.*

---

### 6. Provas de Conceito e Melhorias Futuras

Durante o desenvolvimento, foram exploradas funcionalidades adicionais que podem ser integradas futuramente para enriquecer a aplicação.

#### a. Sugestões de Roteiro com Inteligência Artificial (Prova de Conceito)

Foi desenvolvido um microsserviço `sugestao-service` (atualmente desativado no `docker-compose.yml`) que se comunica com um modelo de linguagem grande (LLM) local através do **Ollama**.

-   **Fluxo:** O serviço é capaz de receber um tema de viagem (ex: "praias no nordeste") e usar o LLM para gerar sugestões de roteiro, retornando-as em formato JSON.
-   **Status:** O serviço foi testado com sucesso via Postman, validando a comunicação com a IA.
-   **Próximos Passos:**
    1.  Integrar o `sugestao-service` ao `docker-compose.yml`.
    2.  Criar um componente no front-end para que o usuário possa solicitar sugestões.
    3.  Fazer o `roteiro-service` atuar como um gateway, recebendo a requisição do front-end e repassando-a para o `sugestao-service` via HTTP Client.

#### b. Outras Melhorias

-   **Testes Unitários e de Integração:** Expandir a cobertura de testes para garantir a robustez dos serviços.
-   **Refinamento da Interface:** Melhorar a experiência do usuário (UX) e o design da interface (UI) no front-end.
