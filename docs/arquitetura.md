# Arquitetura hexagonal

O código separa regras e casos de uso de detalhes de transporte, mensageria e armazenamento. As dependências apontam para dentro: os adaptadores dependem das portas da aplicação, e a aplicação não importa classes do Spring, MongoDB ou Kafka.

## Pacotes

- `domain`: modelo `Payment`, identificadores e exceções do domínio.
- `application.port.in`: casos de uso, comandos e respostas expostos aos adaptadores de entrada.
- `application.port.out`: contratos que a aplicação requer de persistência e publicação de eventos.
- `application.service`: implementação dos casos de uso e mapeamento para respostas.
- `infrastructure.adapter.in.rest`: endpoints HTTP e DTO de entrada.
- `infrastructure.adapter.in.kafka`: consumidor do evento de pagamento.
- `infrastructure.adapter.out.kafka`: publicação de eventos no Kafka.
- `infrastructure.adapter.out.persistence`: implementação MongoDB da porta de repositório e mapeador de entidade.

## Fluxos

### Solicitação HTTP e processamento assíncrono

```text
PaymentController
  -> PaymentManagementUseCase
  -> PaymentManagementService
  -> PaymentEventPublisher
  -> KafkaPaymentEventPublisher
  -> Kafka
  -> KafkaPaymentMessageConsumer
  -> PaymentManagementUseCase.registerPayment
  -> PaymentRepository
  -> MongoPaymentRepositoryAdapter
  -> MongoDB
```

O caso de uso cria um `transactionId` antes de publicar a mensagem. A resposta HTTP contém esse identificador; a persistência ocorre de forma assíncrona, então a consulta pode não encontrar o pagamento imediatamente.

### Consulta

```text
PaymentController
  -> PaymentManagementUseCase
  -> PaymentManagementService
  -> PaymentRepository
  -> MongoPaymentRepositoryAdapter
  -> MongoDB
```

## Regras de dependência

- Os controladores e consumidores chamam a porta de entrada `PaymentManagementUseCase`.
- O serviço de aplicação depende somente do domínio e das portas de saída.
- Adaptadores externos implementam as portas de saída; a interface Spring Data Mongo permanece dentro do adaptador.
- `PaymentMessage` é o contrato de transporte Kafka e não é usado como entidade de domínio ou de persistência.

## Limites atuais

- A persistência assíncrona pode tornar uma leitura imediata eventualmente consistente.
- O modelo HTTP ainda usa campos numéricos para PAN e CVV e expõe dados de cartão nas respostas; esse contrato precisa de revisão antes de uso com dados reais.
- Ainda faltam validação formal, idempotência, testes de integração com Kafka/MongoDB e tratamento operacional de falhas de publicação.
