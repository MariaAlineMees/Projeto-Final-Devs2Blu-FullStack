# --- ESTÁGIO 1: Build com Maven ---
# Usa uma imagem com Maven e JDK 17 para compilar o projeto
FROM maven:3.8.5-openjdk-17 AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo de definição do projeto Maven
COPY pom.xml .

# Copia o wrapper do Maven
COPY .mvn/ .mvn
COPY mvnw .
COPY mvnw.cmd .

# Copia todo o código fonte do projeto
COPY src ./src

# Executa o build do Maven para criar o arquivo .jar
# O -DskipTests pula a execução dos testes para acelerar o build
RUN mvn package -DskipTests

# --- ESTÁGIO 2: Imagem Final ---
# CORRIGIDO: Imagem correta
# Usa uma imagem leve apenas com o Java 17 para rodar a aplicação
FROM eclipse-temurin:17-jdk-alpine

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo .jar gerado no estágio de build
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta 8080
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java","-jar","app.jar"]
