# Usa a imagem do OpenJDK para o Spring Boot
FROM openjdk:17-jdk-alpine

RUN apk add --no-cache ffmpeg

# Define o diretório de trabalho no container
WORKDIR /app

# Copia o jar da aplicação para o container
COPY target/openstreamify.jar /app/openstreamify.jar

# Expõe a porta em que a aplicação irá rodar
EXPOSE 8080

# Define o comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "openstreamify.jar"]
