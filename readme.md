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

curl -X POST http://localhost:8080/api/items \
-H "Content-Type: application/json" \
-d '{"name": "Laptop Pro"}

Response:

{
"id": 1,
"name": "Laptop Pro"
}

#### 2. Listar Todos os Items

curl -X GET http://localhost:8080/api/items

Response: [
{
"id": 1,
"name": "Laptop Pro"
}
]

### Users (Utilizadores)

#### 1. Criar um Utilizador

curl -X POST http://localhost:8080/api/users \
-H "Content-Type: application/json" \
-d '{"name": "Ana Silva", "email": "ana@example.com"}'

Response: {
"id": 1,
"name": "Ana Silva",
"email": "ana.silva@example.com"
}

#### 2. Listar Todos os Utilizadores

curl -X GET http://localhost:8080/api/users

Response: [
{
"id": 1,
"name": "Ana Silva",
"email": "ana.silva@example.com"
}
]

### Stock Movements

#### 1. Criar Entrada de Stock

curl -X POST http://localhost:8080/api/stock-movements \
-H "Content-Type: application/json" \
-d '{"itemId": 1, "quantity": 10}'

Response: {
"id": 1,
"creationDate": "2026-06-06T12:00:00",
"item": {
"id": 1,
"name": "Laptop Pro"
},
"quantity": 10,
"quantityAvailable": 10,
"allocations": []
}

#### 2. Listar Movimentos de Stock

curl -X GET http://localhost:8080/api/stock-movements

Response: [
{
"id": 1,
"creationDate": "2026-06-06T12:00:00",
"item": {
"id": 1,
"name": "Laptop Pro"
},
"quantity": 10,
"quantityAvailable": 10,
"allocations": []
}
]

### Orders

#### 1. Criar Encomenda

curl -X POST http://localhost:8080/api/orders \
-H "Content-Type: application/json" \
-d '{"itemId": 1, "quantity": 5, "userId": 1}'

Response: {
"id": 1,
"creationDate": "2026-06-06T12:05:00",
"itemId": 1,
"itemName": "Laptop Pro",
"quantityRequested": 5,
"quantityFulfilled": 5,
"completed": true,
"completionPercentage": 100.0,
"allocations": [
{
"stockMovementId": 1,
"quantityAllocated": 5
}
]
}

#### 2. Listar Todas as Encomendas

curl -X GET http://localhost:8080/api/orders

Response: [
{
"id": 1,
"creationDate": "2026-06-06T12:05:00",
"itemId": 1,
"itemName": "Laptop Pro",
"quantityRequested": 5,
"quantityFulfilled": 5,
"completed": true,
"completionPercentage": 100.0,
"allocations": [
{
"stockMovementId": 1,
"quantityAllocated": 5
}
]
}
]

#### 3. Consultar Encomenda por ID

curl -X GET http://localhost:8080/api/orders/1

Response: {
"id": 1,
"creationDate": "2026-06-06T12:05:00",
"itemId": 1,
"itemName": "Laptop Pro",
"quantityRequested": 5,
"quantityFulfilled": 5,
"completed": true,
"completionPercentage": 100.0,
"allocations": [
{
"stockMovementId": 1,
"quantityAllocated": 5
}
]
}