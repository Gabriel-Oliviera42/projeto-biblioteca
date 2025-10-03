# Usa uma imagem base oficial que já tem Java 21 e Maven instalados
FROM maven:3.9-eclipse-temurin-21

# Define o diretório de trabalho dentro do nosso ambiente virtual
WORKDIR /app

# Copia todos os arquivos do seu projeto para dentro do ambiente
COPY . .

# Executa o comando de build do Maven para criar o arquivo .jar
RUN mvn clean install

# Expõe a porta 8080, que é a porta que nossa aplicação usa
EXPOSE 8080

# Define o comando que será executado quando o servidor iniciar
CMD ["java", "-jar", "target/biblioteca-0.0.1-SNAPSHOT.jar"]