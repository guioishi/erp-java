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

docker-compose down -v

mvn clean package -DskipTests

docker-compose build --no-cache

docker-compose up

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

## 🚀 Próximos Passos

- 🔐 Implementar autenticação (JWT)
  - Login de usuário
  - Proteção de endpoints
  - Controle de acesso

- 📚 Melhorar documentação com Swagger
  - Adicionar descrições nos endpoints (`@Operation`)
  - Documentar respostas (`@ApiResponse`)
  - Incluir exemplos nos DTOs (`@Schema`)
  - Organizar endpoints com tags
  - Configurar autenticação no Swagger (Bearer Token)
