# Como Executar o Projeto

## Pré-requisitos

Java Development Kit (JDK) 8 instalado.

Maven instalado (ou podes usar o mvnw se incluíres o Maven Wrapper).

## Passos para Correr a Aplicação 

Clonar o repositório ou abrir a pasta do projeto.

Abrir um terminal na raiz do projeto e executar o comando para compilar e arrancar a aplicação:

_mvn clean spring-boot:run_

A aplicação irá arrancar na porta 8080, utilizando uma base de dados em memória H2 (jdbc:h2:mem:testdb).

## Como Executar os Testes Automatizados

Abrir um terminal na raiz do projeto e executar o comando

_mvn test_

## Documentação da API (Endpoints)

### Items

#### 1. Criar um Item

POST /api/items

Content-Type: application/json

Payload:{ "name": "Laptop Pro" }

Resposta Sucesso (200 OK): [ { "id": 1, "name": "Laptop Pro" } ]

#### 2. Listar Todos os Items

GET /api/items

Resposta Sucesso (200 OK): [ { "id": 1, "name": "Laptop Pro" } ]

### Users (Utilizadores)

#### 1. Criar um Utilizador

POST /api/users

Content-Type: application/json

Payload: [ {
"name": "Ana Silva",
"email": "ana.silva@example.com"
} ]

Resposta Sucesso (200 OK): [ {
"id": 1,
"name": "Ana Silva",
"email": "ana.silva@example.com"
} ]

#### 2. Listar Todos os Utilizadores

GET /api/users

Resposta Sucesso (200 OK): [ { "id": 1, "name": "Ana Silva", "email": "ana.silva@example.com" } ]

### Stock Movements

#### 1. Criar Entrada de Stock

POST /api/stock-movements

Content-Type: application/json

Payload: [ {
"itemId": 1,
"quantity": 10
} ]

Resposta Sucesso (200 OK): [ {
"id": 1,
"creationDate": "2026-06-06T12:00:00",
"item": { "id": 1, "name": "Laptop Pro" },
"quantity": 10,
"quantityAvailable": 10,
"allocations": []
} ]

#### 2. Listar Movimentos de Stock

GET /api/stock-movements

Resposta Sucesso (200 OK): [ {
"id": 1,
"creationDate": "2026-06-06T12:00:00",
"item": { "id": 1, "name": "Laptop Pro" },
"quantity": 10,
"quantityAvailable": 10,
"allocations": []
} ]

### Orders

#### 1. Criar Encomenda
POST /api/orders

Content-Type: application/json

Payload: [{
"itemId": 1,
"quantity": 4,
"userId": 1
}] 

Resposta Sucesso: [{
"id": 1,
"creationDate": "2026-06-06T12:05:00",
"item": { "id": 1, "name": "Laptop Pro" },
"quantityRequested": 4,
"quantityFulfilled": 4,
"user": { "id": 1, "name": "Ana Silva", "email": "ana.silva@example.com" },
"completed": true,
"allocations": [
{
"id": 1,
"quantityAllocated": 4
}
],
"completionPercentage": 100.0
}]

#### 2. Listar Todas as Encomendas
GET /api/orders

Resposta Sucesso (200 OK): [{
"id": 1,
"creationDate": "2026-06-06T12:05:00",
"item": { "id": 1, "name": "Laptop Pro" },
"quantityRequested": 4,
"quantityFulfilled": 4,
"user": { "id": 1, "name": "Ana Silva", "email": "ana.silva@example.com" },
"completed": true,
"allocations": [
{
"id": 1,
"quantityAllocated": 4
}
],
"completionPercentage": 100.0
}]

#### 3. Consultar Encomenda por ID
GET /api/orders/{id}

Resposta Sucesso (200 OK): [{
"id": 1,
"creationDate": "2026-06-06T12:05:00",
"item": { "id": 1, "name": "Laptop Pro" },
"quantityRequested": 4,
"quantityFulfilled": 4,
"user": { "id": 1, "name": "Ana Silva", "email": "ana.silva@example.com" },
"completed": true,
"allocations": [mvn
{
"id": 1,
"quantityAllocated": 4
}
],
"completionPercentage": 100.0
}]