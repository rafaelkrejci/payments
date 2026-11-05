# Documentação técnica

Este diretório reúne a documentação complementar do projeto `payments`.

## Conteúdo

- [Arquitetura do sistema](arquitetura.md)
- [API REST](api.md)
- [Ambientes e configurações](ambientes.md)
- [Guia de implantação em produção](deploy-producao.md)
- [Validação e segurança](seguranca-validacao.md)
- [Roadmap de evolução](roadmap.md)

## Objetivo

A documentação aqui apresentada complementa o `README.md` principal com detalhes de arquitetura, integração, governança de ambiente e plano de evolução da aplicação.

## Visão rápida

A aplicação recebe pagamentos via API HTTP, publica uma mensagem em Kafka, processa a mensagem em um consumidor e salva o registro em MongoDB. O projeto usa portas e adaptadores para separar casos de uso dos detalhes de transporte e persistência.

## Fluxo principal

```text
Cliente HTTP
  -> POST /api/v1/payments/
  -> PaymentManagementUseCase
  -> Kafka
  -> KafkaPaymentMessageConsumer
  -> PaymentManagementUseCase
  -> PaymentRepository
  -> MongoDB
```

## Executando o projeto completo

```bash
docker compose up -d
```

Esse comando inicia MongoDB, Kafka, Kafka UI e collector. Inicie a API separadamente com `./mvnw spring-boot:run`; ela fica disponível em `http://localhost:8080`. O Kafka UI fica em `http://localhost:8090`.

## Quando consultar cada documento

- `arquitetura.md`: entender responsabilidades e fluxo técnico.
- `api.md`: validar contratos e exemplos de chamada.
- `ambientes.md`: configurar dev, test e prod.
- `deploy-producao.md`: preparar, publicar, validar e reverter uma implantação.
- `seguranca-validacao.md`: revisar requisitos de qualidade e proteção.
- `roadmap.md`: planejar próximas entregas e melhorias.
