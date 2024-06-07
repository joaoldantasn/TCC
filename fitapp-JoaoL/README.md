# Fit-App API
API de gamificação para treinos de academia.

## Tecnologias
- [Java 17](https://docs.oracle.com/en/java/javase/17/)
- [Spring Boot v3.2.3](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://docs.spring.io/spring-data/data-jpa/docs/current/reference/html/#repositories)
- [Spring Security](https://docs.spring.io/spring-security/reference/index.html)
- [JSON Web Token](https://jwt.io/introduction)
- [Lombok](https://projectlombok.org/features/)

## Como Executar

- Construir o projeto:

```
./mvnw clean package
```

- Executar a aplicação:

```
java -jar target/fit-app-0.0.1-SNAPSHOT.jar
```

A API poderá ser acessada em [localhost:8080](http://localhost:8080).

## API Endpoints

Apenas endpoint de Login é pública. Todas as outras é necessario estar autenticado. 
O tipo de autenticação é `Bearer Token`. 

#### Endpoints de Autenticação

- POST `/api/auth/login`
```json
{
  "email": "admin@gmail.com",
  "senha": "admin"
}

Resposta:
{
  "token": "{$VALID-TOKEN}",
  "expiraEm": 600,
  "usuario": {
    "email": "admin@gmail.com"
  }
}
```

#### Endpoints de Usuário

- GET `/api/usuarios/info`
- POST `/api/usuarios/registrar`
```json
{
    "nome": "Nome",
    "email": "email@gmail.com",
    "senha": "senha",
    "telefone": "+1234567890",
    "altura": 1.75,
    "peso": 70.5,
    "foto_perfil": "url_da_foto"
}
```
- PATCH `/api/usuarios`

Observação: Esta endpoint é um PATCH isto significa que não é necessario especificar certos campos no JSON. Apenas é escrito o que será modificado, o resto pode ser omisso.
```json
{
    "nome": "nome atualizado",
    "senha": "senha atualizada",
    "telefone": "+1234567890",
    "altura": 1.75,
    "peso": 70.5,
    "foto_perfil": "url_da_foto atualizado"
}
```
- POST `/api/usuarios/desafios/criar`
```text
Tipos de Desafios:
    CARDIO - Caso usado como Enum no JSON por padrão o codigo é 00
    EXERCICIOS - Caso usado como Enum no JSON por padrão o codigo é 01
    PRESENCA - Caso usado como Enum no JSON por padrão o codigo é 02
```
```json
{
    "nome": "Nome Desafio",
    "descricao": "Descrição Desafio",
    "premiacao": "Premiacao Desafio",
    "imagem": "imagem_url",
    "tipo_desafio": "CARDIO",
    "data_inicio": "2024-02-22",
    "data_final": "2024-02-26"
}
```
- DELETE `/api/usuarios/desafios/deleta/{desafioNome}`
- PUT `/api/usuarios/desafios/entrar/{nomeDesafio}`
- PUT `/api/usuarios/desafios/sair/{desafioNome}`
- PATCH `/api/usuarios/desafios/atualizar/{desafioNome}`

Modificar o tipo de desafio após sua criação não vai modificar a pontuação dos treinos já postados.
Observação: Esta endpoint é um PATCH isto significa que não é necessario especificar certos campos no JSON. 
Apenas é escrito o que será modificado, o resto pode ser omisso. Siga exemplo.
```json
{
  "nome": "Nome Desafio Atualizado",
  "descricao": "Descrição Desafio Atualizado",
  "premiacao": "Premiacao Desafio Atualizado",
  "imagem": "imagem_url_atualizado",
  "tipo_desafio": "EXERCICIOS",
  "data_inicio": "2024-02-22",
  "data_final": "2024-02-26"
}
```
Este é um exemplo válido para modificar apenas `descricao` e `premiacao`.
```json
{
  "descricao": "Descrição Desafio Atualizado",
  "premiacao": "Premiacao Desafio Atualizado"
}
```
- PUT `/api/usuarios/desafios/{desafioNome}/postar-treino`

Os exercicios especificados ao postar o treino devem existir dentro do banco de dados. 
Se não uma exceção será lançada.
```json
{
    "nome": "nome trieno",
    "descricao": "descricao treino",
    "exercicios": [
        {
            "nome": "02 - Crossover"
        },
        {
            "nome": "02 - Pullover"
        },
        {
            "nome": "01 - Cardio"
        }
    ]
}
```

#### Endpoints de Desafio

- GET `/api/desafios/disponiveis`
- GET `/api/desafios/participando`
- GET `/api/desafios/criados`
- GET `/api/desafios/{desafioNome}`
- 
#### Endpoints de Treino

- GET `/api/treinos/desafio/{desafioNome}`
- 