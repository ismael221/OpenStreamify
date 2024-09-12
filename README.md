# Aplicação de Streaming de Filmes

## Descrição

Esta é uma aplicação web desenvolvida em **Spring Boot** para gerenciamento e streaming de filmes. A aplicação inclui funcionalidades de autenticação via **JWT tokens**, **Spring MVC** para manipulação de requisições HTTP e um sistema de controle de acesso baseado em permissões de usuários. Além disso, a aplicação suporta o streaming de filmes utilizando o **HLS (HTTP Live Streaming)**.

### Funcionalidades

- Cadastro de filmes e usuários.
- Sistema de login e registro com autenticação JWT.
- Avaliação de filmes, com possibilidade de adicionar análises e notas.
- Streaming de vídeos em formato HLS.
- Controle de acesso com base em permissões de usuários.
- Endpoints seguros usando autenticação JWT.
- Páginas com Thymeleaf para interações de usuário.
- Monitoramento da aplicação utilizando Grafana e Prometheus em containers Docker.

## Tecnologias Utilizadas

- **Java**: Linguagem principal de desenvolvimento.
- **Spring Boot**: Framework para desenvolvimento da aplicação.
- **Spring Security**: Para autenticação e autorização utilizando JWT.
- **JWT (JSON Web Token)**: Para autenticação segura de APIs.
- **Spring MVC**: Para gerenciamento de requisições HTTP e controle de rotas.
- **HLS (HTTP Live Streaming)**: Para o streaming de vídeos.
- **Thymeleaf**: Motor de templates para renderizar as páginas HTML.
- **ModelMapper**: Para conversão de entidades e DTOs.
- **Docker**: Para containerização da aplicação e serviços de monitoramento.
- **MySQL**: Banco de dados usado para persistência dos dados de filmes e usuários.
- **Grafana**: Plataforma de análise e monitoramento.
- **Prometheus**: Sistema de monitoramento e alerta.

## Requisitos do Sistema

- **JDK 17** ou superior
- **Maven** 3.6+
- **MySQL** ou qualquer outro banco de dados relacional
- **Docker** (opcional para rodar a aplicação e serviços de monitoramento)
- **Postman** (para testar os endpoints da API)

## Configuração e Instalação

1. Clone o repositório:
   ```bash
   git clone https://github.com/ismael221/filmes


2. Configure o banco de dados no arquivo `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/nome_do_banco
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```

3. Para rodar o projeto, utilize o seguinte comando no terminal na raiz do projeto:
   ```bash
   mvn spring-boot:run
   ```

4. Para rodar o Grafana e o Prometheus em containers Docker, execute:
   ```bash
   docker-compose up -d
   ```

5. Acesse a aplicação no navegador:
   ```
   http://localhost:8080
   ```

6. Acesse o Grafana para monitoramento:
   ```
   http://localhost:3000
   ```
   - Usuário: `admin`
   - Senha: `admin`

7. Acesse o Prometheus para visualização dos dados coletados:
   ```
   http://localhost:9090
   ```

## Endpoints Principais

### Autenticação

- **POST** `/auth/register`: Cadastro de usuários.
- **POST** `/auth/login`: Autenticação de usuários e geração de JWT.

### Filmes

- **GET** `/api/v1/filme/listar`: Lista todos os filmes.
- **POST** `/api/v1/filme/adicionar`: Cadastra um novo filme.
- **GET** `/api/v1/filme/rid/{rid_filme}`: Detalhes de um filme específico.
- **POST** `/api/v1/analise/adicionar`: Adiciona uma análise para um filme.

### Streaming

- **GET** `/api/v1/media/hls/{filename}.m3u8`: Faz o streaming do vídeo HLS baseado no nome do arquivo.

### Exemplo de Requisição Autenticada com JWT

Todas as rotas, exceto o login e o registro, exigem um token JWT para autenticação. Para acessar as rotas protegidas, você precisa passar o token no header da requisição:

```http
Authorization: Bearer <seu-jwt-token>
```

### Swagger UI

Para verificar todos os endpoints disponíveis e suas descrições, acesse o Swagger UI:
```
http://localhost:8080/swagger-ui.html
```

## Estrutura do Projeto

- `src/main/java/com/ismael/movies`: Contém as classes Java, incluindo os controladores, serviços, modelos e repositórios.
- `src/main/resources/templates`: Contém as páginas HTML renderizadas pelo Thymeleaf.
- `src/main/resources/static`: Contém arquivos estáticos, como CSS e JavaScript.
- `src/main/resources/application.properties`: Configurações da aplicação.

## Segurança

A aplicação utiliza **JWT tokens** para autenticação e autorização. Ao fazer login, o usuário recebe um token que deve ser incluído no cabeçalho de todas as requisições subsequentes às rotas protegidas.

## Melhorias Futuras

- Integração com serviços de mensageria para processamento em alta escala.
- Implementação de CDN para melhorar a performance do streaming em grande volume.
- Suporte a múltiplas qualidades de vídeo no HLS.
- Implementação de cache para otimização do sistema.

## Contribuindo

Contribuições são bem-vindas! Sinta-se à vontade para abrir uma **issue** ou enviar um **pull request**.

## Licença

Este projeto está licenciado sob os termos da licença MIT. Veja o arquivo [LICENSE](./LICENSE) para mais detalhes.
