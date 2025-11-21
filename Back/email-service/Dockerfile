# Usar uma imagem base oficial do OpenJDK para Java 17
FROM eclipse-temurin:17-jdk-focal

# Definir o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copiar o arquivo JAR do seu projeto Spring Boot para o contêiner
# O nome do arquivo JAR será email-service-*.jar (assumindo que você usa Maven/Gradle padrão)
# Você precisará compilar seu projeto (mvn clean package) antes de construir a imagem Docker
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expor a porta em que o serviço Spring Boot será executado
EXPOSE 8081

# Comando para executar a aplicação Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
