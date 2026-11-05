# API REST

## Base URL

```text
http://localhost:8080/api/v1/payments
```

## Endpoints

### 1) Criar pagamento

#### Requisição

```http
POST /api/v1/payments/
Content-Type: application/json
```

#### Payload de exemplo

```json
{
  "pan": 4111111111111111,
  "cvv": 123,
  "expirationDate": "12/30",
  "cardHolderName": "Maria",
  "cardHolderLastName": "Silva",
  "cardHolderEmail": "maria.silva@email.com",
  "cardHolderPhone": "+5511999999999",
  "sku": "SKU-001",
  "amount": 1500
}
```

#### Resposta

```json
{
  "transactionId": "identificador-gerado"
}
```

#### Observações

- A operação publica a mensagem no Kafka e retorna o identificador de transação.
- A gravação no MongoDB é assíncrona; uma consulta imediata pode retornar `404` até o consumidor persistir o evento.
- Ainda não há validação formal de campos, nem restrições de domínio no contrato atual.

### 2) Buscar pagamento por identificador

#### Requisição

```http
GET /api/v1/payments/{transactionId}
```

#### Resposta bem-sucedida

```json
{
  "transactionId": "...",
  "pan": 4111111111111111,
  "cvv": 123,
  "expirationDate": "12/30",
  "cardHolderName": "Maria",
  "cardHolderLastName": "Silva",
  "cardHolderEmail": "maria.silva@email.com",
  "cardHolderPhone": "+5511999999999",
  "sku": "SKU-001",
  "amount": 1500
}
```

#### Resposta quando não existe

```http
404 Not Found
```

### 3) Listar pagamentos

#### Requisição

```http
GET /api/v1/payments/
```

Parâmetros de consulta opcionais:

| Parâmetro | Padrão | Limites |
| --- | ---: | --- |
| `page` | `0` | Deve ser maior ou igual a zero |
| `size` | `20` | De `1` a `100` |

Os resultados são ordenados pelo identificador MongoDB em ordem crescente.
Parâmetros inválidos retornam `400 Bad Request`.

Exemplo:

```http
GET /api/v1/payments/?page=1&size=20
```

#### Resposta

```json
{
  "content": [
    {
      "transactionId": "...",
      "pan": 4111111111111111,
      "cvv": 123,
      "expirationDate": "12/30",
      "cardHolderName": "Maria",
      "cardHolderLastName": "Silva",
      "cardHolderEmail": "maria.silva@email.com",
      "cardHolderPhone": "+5511999999999",
      "sku": "SKU-001",
      "amount": 1500
    }
  ],
  "page": 1,
  "size": 20,
  "totalElements": 21,
  "totalPages": 2
}
```

## Exemplo com curl

```bash
curl -X POST http://localhost:8080/api/v1/payments/ \
  -H "Content-Type: application/json" \
  -d '{
    "pan": 4111111111111111,
    "cvv": 123,
    "expirationDate": "12/30",
    "cardHolderName": "Maria",
    "cardHolderLastName": "Silva",
    "cardHolderEmail": "maria.silva@email.com",
    "cardHolderPhone": "+5511999999999",
    "sku": "SKU-001",
    "amount": 1500
  }'
```

```bash
curl 'http://localhost:8080/api/v1/payments/?page=0&size=20'
```

```bash
curl http://localhost:8080/api/v1/payments/{id}
```

## Observações sobre o contrato atual

- A API usa campos numéricos simples para `pan`, `cvv` e `amount`.
- Há ausência de restrições como:
  - tamanho mínimo/máximo
  - formato de cartão
  - e-mail e telefone válidos
  - moeda/valor monetário com decimal
- O retorno de dados sensíveis deve ser revisado antes de qualquer uso com cartões reais.

## Melhorias sugeridas para o contrato

1. Adicionar Bean Validation (`@NotNull`, `@Email`, `@Pattern`, etc.).
2. Usar `String` para `pan` e `cvv` com mascaramento e validação.
3. Criar DTOs separados para request/response com regras específicas.
4. Adicionar links HATEOAS de navegação às páginas, se necessário.
5. Adicionar códigos de erro padronizados e mensagens sem revelar detalhes sensíveis.
