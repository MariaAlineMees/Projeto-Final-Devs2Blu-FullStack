# Registro de Desafios no Deploy - Projeto "Meu Roteiro"

Este documento registra as tentativas e os desafios encontrados durante o processo de deploy da aplicação na plataforma Render.com. O objetivo é documentar o aprendizado e os obstáculos técnicos relacionados à configuração de uma arquitetura de microsserviços em um ambiente de nuvem gerenciado.

---

### Tentativa 1: Deploy Manual via UI

-   **Abordagem:** Criação de cada serviço (`roteiro-service`, `email-service`, `rabbitmq`, `roteiro-front`) e do banco de dados (`PostgreSQL`) individualmente através da interface de usuário do Render.
-   **Desafio:** Falha na comunicação entre os serviços. O `roteiro-front` (Nginx) não conseguia encontrar o `roteiro-service` na rede interna.
-   **Erro Principal:** `host not found` ou `could not be resolved`.
-   **Análise:** Os serviços, quando criados separadamente, não compartilham a mesma rede privada de forma automática e garantida, resultando em falhas de resolução de DNS interno.

---

### Tentativa 2: Comunicação via URL Pública

-   **Abordagem:** Alterar a configuração do Nginx no `roteiro-front` para apontar para a URL pública do `roteiro-service` (ex: `https://roteiro-service.onrender.com`).
-   **Desafio:** A comunicação passou a ser encontrada, mas a conexão falhava na negociação de segurança.
-   **Erro Principal:** `SSL_do_handshake() failed`.
-   **Análise:** O Nginx, atuando como proxy, tentava estabelecer uma conexão segura (HTTPS) com o backend, mas a forma como a rede do Render gerencia o tráfego interno causava um conflito no handshake SSL.

---

### Tentativa 3: Configuração de Rede no Nginx (`resolver`)

-   **Abordagem:** Voltar a usar o nome do serviço interno (`http://roteiro-service:8080`) e adicionar diretivas de `resolver` ao `nginx.conf` para instruir o Nginx a usar o DNS interno da plataforma.
-   **Desafio:** Mesmo com o `resolver` configurado, a conexão ainda falhava, mas com um erro diferente e mais sutil.
-   **Erro Principal:** `roteiro-service could not be resolved (110: Operation timed out)`.
-   **Análise:** O erro mudou de "não encontrado" para "demorou demais para responder". Isso indicou que o problema estava relacionado ao "cold start" dos serviços no plano gratuito do Render. O serviço de backend demorava para "acordar", e o Nginx, por padrão, desistia de esperar pela resposta do DNS muito rapidamente. Aumentar os `timeouts` de conexão não foi suficiente, pois o `resolver_timeout` também precisava ser ajustado.

---

### Tentativa 4: Infraestrutura como Código (`render.yaml`)

-   **Abordagem:** Adotar a abordagem de "Blueprint" do Render, definindo toda a infraestrutura (banco de dados e todos os serviços) em um único arquivo `render.yaml`. Esta é a abordagem recomendada pela plataforma para garantir a consistência da rede.
-   **Desafio:** A criação do `render.yaml` enfrentou múltiplos erros de sintaxe e validação específicos da plataforma.
-   **Erros Principais:**
    1.  **Exposição de Segredos:** A chave da API do SendGrid foi colocada diretamente no arquivo, fazendo com que o GitHub bloqueasse o `push` por razões de segurança.
    2.  **Erros de Sintaxe do Render:**
        -   `field postgre not found`: A estrutura para definir o banco de dados estava incorreta.
        -   `non-docker... runtime must have startCommand`: Faltou a diretiva `env: image` para o serviço do RabbitMQ.
        -   `routes only supported for static web services`: Uso incorreto da diretiva `routes` em um serviço Docker.
        -   `fromService.type empty`: Faltou especificar o `type` do serviço ao referenciá-lo.
    3.  **Conflito de Variáveis de Ambiente:** O `render.yaml` tentou injetar variáveis (`USERNAME`, `PASSWORD`) que conflitavam com a `connectionString` completa, causando um erro de inicialização no Spring Boot (`URL must start with 'jdbc'`).
-   **Análise:** A abordagem de Blueprint é a mais robusta, mas requer uma sintaxe muito precisa e um entendimento claro de como o Render provisiona os recursos e injeta as variáveis de ambiente. Cada erro de validação exigiu um ajuste fino no arquivo `render.yaml`.

---

### Conclusão

O deploy de uma aplicação de microsserviços em uma nova plataforma de nuvem é um desafio complexo que vai além do código da aplicação. Os principais obstáculos foram relacionados à configuração de rede, resolução de DNS, gerenciamento de segredos e particularidades da sintaxe da ferramenta de "Infraestrutura como Código" (`render.yaml`) da plataforma. O projeto está 100% funcional em um ambiente local controlado pelo Docker Compose, e os aprendizados adquiridos aqui seriam a base para uma futura tentativa de deploy bem-sucedida.
