# 1. Imagem base: Usamos uma imagem oficial do Java para rodar a aplicação
FROM eclipse-temurin:17-jdk-focal

# 2. Argumento para o arquivo JAR
ARG JAR_FILE=target/*.jar

# 3. Copia o arquivo JAR (seu programa compilado) para dentro do contêiner
COPY ${JAR_FILE} app.jar

# 4. Define a porta que a aplicação Spring estará usando
EXPOSE 8080

# 5. Comando para executar a aplicação Spring Boot
ENTRYPOINT ["java", "-jar", "/app.jar"]