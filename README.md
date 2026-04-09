# 🚀 ERP Java - API REST

Sistema backend desenvolvido com **Spring Boot** para gerenciamento de **Produtos** e **Pedidos**, com regras de negócio, filtros dinâmicos e paginação.

---

# 📌 Tecnologias

- Java 21
- Spring Boot 3
- Spring Data JPA
- Bean Validation
- QueryDSL
- PostgreSQL
- Docker / Docker Compose
- Maven
- JUnit + Mockito

---

# 📂 Estrutura do Projeto

```
com.senior.erp
├── controller
├── service
├── repository
├── entity
├── dto
```

---

# ⚙️ Como rodar o projeto

## 1. Subir banco (Docker)

```bash
docker-compose up -d db
```

ou:

```bash
docker run --name postgres-erp \
  -e POSTGRES_DB=erp \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres
```

---

## 2. Rodar aplicação

```bash
mvn spring-boot:run
```

ou:

```bash
docker-compose up -d
```

---

# 🗄️ Banco de Dados

- PostgreSQL
- Hibernate cria as tabelas automaticamente (`ddl-auto=create`)

---

# 📦 Endpoints

---

## 🟢 Produtos

### Criar

```
POST /products
```

```json
{
  "name": "Notebook",
  "price": 5000,
  "type": "PRODUCT"
}
```

---

### Listar (com filtro + paginação)

```
GET /products?page=0&size=10&name=note&type=PRODUCT&active=true
```

---

### Buscar por ID

```
GET /products/{id}
```

---

### Atualizar

```
PUT /products/{id}
```

---

### Deletar

```
DELETE /products/{id}
```

⚠️ Não pode deletar se estiver vinculado a pedidos

---

### Ativar/Desativar

```
PATCH /products/{id}/toggle-active
```

---

## 🔵 Pedidos

### Criar pedido

```
POST /orders
```

```json
{
  "discount": 10,
  "items": [
    {
      "productId": "UUID",
      "quantity": 2
    }
  ]
}
```

---

### Listar (com filtro + paginação)

```
GET /orders?page=0&size=10&status=OPEN&minTotal=100&maxTotal=500
```

---

### Buscar por ID

```
GET /orders/{id}
```

---

### Atualizar

```
PUT /orders/{id}
```

⚠️ Pedido fechado não pode ser alterado

---

### Fechar pedido

```
PATCH /orders/{id}/close
```

---

### Deletar

```
DELETE /orders/{id}
```

---

# 🔍 Filtros

## ProductFilter

- name
- type (PRODUCT | SERVICE)
- active

## OrderFilter

- status (OPEN | CLOSED)
- minTotal
- maxTotal

---

# 🧠 Regras de Negócio

## Produtos

- Produto inicia ativo
- Produto inativo não pode ser usado em pedidos
- Produto não pode ser:
  - alterado
  - deletado  
    se estiver vinculado a pedidos

---

## Pedidos

- Status: OPEN / CLOSED
- Apenas OPEN pode ser alterado
- Desconto aplicado apenas em produtos
- Serviços não recebem desconto
- Total calculado automaticamente

---

# 📤 Exemplo de resposta

```json
{
  "id": "uuid",
  "status": "OPEN",
  "total": 9000,
  "items": [
    {
      "productId": "uuid",
      "productName": "Notebook",
      "price": 5000,
      "quantity": 1,
      "type": "PRODUCT"
    }
  ]
}
```

---

# ❌ Tratamento de erros

```json
{
  "timestamp": "2026-04-08T10:00:00",
  "status": 400,
  "error": "Business Rule Error",
  "message": "Produto não encontrado"
}
```

---

# 🧪 Testes

Rodar testes:

```bash
mvn test
```

Cobertura:

- ProductService
- OrderService

---

# 🐳 Docker

Subir tudo:

```bash
docker-compose up -d
```

Parar:

```bash
docker-compose down
```

Resetar banco:

```bash
docker-compose down -v
docker-compose up -d
```

---
