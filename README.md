# MS-AV-01 — Criação de Endpoints

Projeto desenvolvido para a avaliação **MS-AV-01** da disciplina de Microservices Development.

A aplicação disponibiliza endpoints para cadastro de músicas, gerenciamento de playlists, associação entre playlists e músicas e registro de reproduções. O `ApiController` simula a orquestração entre serviços por meio de clientes OpenFeign.

## Tecnologias

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Bean Validation
- Spring Cloud OpenFeign
- H2 Database
- Maven

## Pré-requisitos

Para executar o projeto, é necessário ter instalado:

- JDK 17 ou superior;
- Maven.

Confirme as instalações com:

```bash
java -version
mvn -version
```

## Como executar

No terminal, acesse a pasta do projeto e execute:

```bash
mvn spring-boot:run
```

Após a inicialização, a API estará disponível em:

```text
http://localhost:8080
```

O arquivo `data.sql` cria as tabelas e insere os dados iniciais automaticamente a cada inicialização.

## Banco H2

O console do H2 pode ser acessado em:

```text
http://localhost:8080/h2-console
```

Utilize os seguintes dados:

```text
JDBC URL: jdbc:h2:mem:testdb
User Name: sa
Password: password
```

Como o banco é mantido em memória, os dados criados durante a execução são descartados quando a aplicação é encerrada.

## Endpoints

### Músicas

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/musicas` | Lista todas as músicas |
| `GET` | `/musicas/{id}` | Busca uma música pelo ID |
| `POST` | `/musicas` | Cadastra uma música |
| `PUT` | `/musicas/{id}` | Atualiza uma música |
| `DELETE` | `/musicas/{id}` | Remove uma música e suas associações |

Exemplo de cadastro:

```json
{
  "titulo": "Imagine",
  "artista": "John Lennon",
  "album": "Imagine",
  "duracao": 183,
  "genero": "Rock"
}
```

### Playlists

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/playlists` | Lista todas as playlists |
| `GET` | `/playlists/{playlistid}` | Busca uma playlist pelo ID |
| `POST` | `/playlists` | Cadastra uma playlist |
| `PUT` | `/playlists/{playlistid}` | Atualiza uma playlist |
| `DELETE` | `/playlists/{playlistid}` | Remove uma playlist e suas associações |
| `GET` | `/playlists/{playlistid}/musicas` | Lista os IDs das músicas da playlist |
| `POST` | `/playlists/{playlistid}/musicas/{musicaId}` | Adiciona uma música à playlist |
| `DELETE` | `/playlists/{playlistid}/musicas/{musicaId}` | Remove uma música da playlist |

Exemplo de cadastro:

```json
{
  "nome": "Clássicos do Rock",
  "descricao": "Grandes clássicos do rock internacional"
}
```

### Reproduções

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/reproducao` | Registra uma reprodução |
| `GET` | `/reproducao/{playlistid}` | Lista as reproduções de uma playlist |
| `GET` | `/reproducao/total/{playlistid}` | Retorna o total de reproduções |

Para registrar uma reprodução, envie apenas o ID da playlist:

```json
{
  "playlistid": 1
}
```

O ID e o horário da reprodução são definidos pelo servidor.

### Orquestração com OpenFeign

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/adicionar/{playlistId}/musicas/{musicaId}` | Valida os recursos e adiciona uma música à playlist via OpenFeign |
| `PUT` | `/api/executar/{playlistId}` | Valida a playlist e registra uma reprodução via OpenFeign |

Embora a avaliação mantenha todos os recursos na mesma aplicação, o `ApiController` não acessa repositories diretamente. Ele chama os endpoints existentes pelos clientes `MusicaClient`, `PlaylistClient` e `ReproducaoClient`.

## Exemplos de chamadas

Adicionar a música 1 à playlist 2:

```bash
curl -X POST http://localhost:8080/api/adicionar/2/musicas/1
```

Executar a playlist 1:

```bash
curl -X PUT http://localhost:8080/api/executar/1
```

Consultar o total de reproduções da playlist 1:

```bash
curl http://localhost:8080/reproducao/total/1
```

## Testes e build

Execute:

```bash
mvn test
```

Para gerar o pacote da aplicação:

```bash
mvn clean package
```
