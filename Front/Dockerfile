# --- STAGE 1: Build da Aplicação Angular ---
FROM node:20-alpine AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia os arquivos package.json e package-lock.json
COPY package.json package-lock.json ./

# Instala as dependências do Node.js
RUN npm install

# Copia todo o código fonte da aplicação
COPY . .

# Compila o projeto Angular com o padrão (usando o script do package.json)
RUN npm run build

# --- STAGE 2: Servir a Aplicação com Nginx ---
FROM nginx:alpine

# Copia o arquivo de configuração do Nginx
COPY nginx-default.conf /etc/nginx/conf.d/default.conf

# Copia o CONTEÚDO do output compilado (que está na pasta /browser) para o Nginx
COPY --from=build /app/dist/roteiro-front/browser/ /usr/share/nginx/html

EXPOSE 80

# Comando para iniciar o Nginx
CMD ["nginx", "-g", "daemon off;"]
