# Payments

API de pagamentos construída com Java 25 e Spring Boot. O projeto usa arquitetura hexagonal: os casos de uso dependem de portas e o código de infraestrutura fornece adaptadores para REST, Kafka e MongoDB.

## Fluxo de processamento

```text
HTTP -> PaymentManagementUseCase -> Kafka -> consumidor
     -> PaymentManagementUseCase -> MongoDB
```

A API gera um `transactionId` e publica o evento. A persistência é assíncrona, então a consulta pode retornar `404` até o consumidor gravar o pagamento.

## Organização

- `domain`: modelo de pagamento, identificadores e exceções.
- `application/port/in`: casos de uso, comandos e respostas.
- `application/port/out`: portas de persistência e publicação de eventos.
- `application/service`: implementação dos casos de uso.
- `infrastructure/adapter/in`: adaptadores REST e Kafka de entrada.
- `infrastructure/adapter/out`: adaptadores Kafka e MongoDB de saída.

Veja [docs/arquitetura.md](docs/arquitetura.md) para os fluxos e regras de dependência.

## Executar e testar

Pré-requisitos: JDK 25, Docker Compose e Maven (ou o Maven Wrapper incluído).

```bash
docker compose up -d
./mvnw spring-boot:run
```

A infraestrutura local inclui MongoDB, Kafka, Kafka UI e um collector. A API usa `localhost:8080`, Kafka UI `localhost:8090` e MongoDB `localhost:27017`. O serviço usa os valores de desenvolvimento definidos em `src/main/resources/application.yaml`; não os reutilize em produção.

Para executar os testes:

```bash
./mvnw test
```

## API

- `POST /api/v1/payments/` — publica um pagamento e retorna o `transactionId`.
- `GET /api/v1/payments/{transactionId}` — consulta por identificador.
- `GET /api/v1/payments/?page=0&size=20` — lista pagamentos persistidos em páginas (tamanho máximo 100).

Consulte [docs/api.md](docs/api.md) para exemplos e formato das respostas.

## Próximos limites conhecidos

A validação formal de entrada, idempotência, autenticação e os controles de segurança necessários para dados de cartão ainda precisam ser implementados. As respostas atuais ainda contêm PAN e CVV; não processe dados reais antes de corrigir esse contrato.

Documentação adicional: [ambientes](docs/ambientes.md), [implantação](docs/deploy-producao.md), [segurança](docs/seguranca-validacao.md), [roadmap](docs/roadmap.md) e [teste de carga](docs/teste-carga.md).
