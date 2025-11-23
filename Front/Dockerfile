# Estágio 1: Build do Angular
FROM node:20-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY . .
RUN npm run build

# Estágio 2: Servidor Nginx
FROM nginx:alpine
# Copia o template da configuração do Nginx
COPY nginx-default.conf.template /etc/nginx/templates/default.conf.template
# Copia os arquivos compilados do Angular para o diretório do Nginx
COPY --from=build /app/dist/roteiro-front/browser/ /usr/share/nginx/html
EXPOSE 80
