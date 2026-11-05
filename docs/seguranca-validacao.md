# Validação e segurança

## 1) Validação de payloads

O projeto atual ainda não aplica validações explícitas nos DTOs de entrada. Isso abre espaço para:

- dados incompletos
- strings vazias
- quantidade inválida
- campos fora do padrão esperado
- payloads malformados

### Recomendação

Adicionar Bean Validation em `PaymentRequest`:

```java
@NotNull
private Integer amount;

@NotBlank
private String cardHolderName;

@Email
private String cardHolderEmail;
```

Também é interessante incluir:

- `@Pattern` para datas e telefone
- `@Size` para campos de texto
- `@Min` e `@Max` para valores numéricos
- validação de `pan` e `cvv`

## 2) Mascaramento e proteção de dados sensíveis

O projeto trata dados financeiros e dados pessoais. Em produção, é essencial:

- evitar log de dados completos do cartão
- mascarar `pan` em respostas
- criptografar dados sensíveis em repouso, quando necessário
- limitar exposição em endpoints de consulta

## 3) Autenticação e autorização

O sistema ainda não possui mecanismo de autenticação. Para um ambiente real, recomenda-se:

- autenticação por JWT ou OAuth2
- autorização por roles/escopos
- proteção dos endpoints administrativos e sensíveis
- logs de auditoria para ações críticas

## 4) Persistência de `transactionId`

O caso de uso gera o identificador antes da publicação e o evento o leva até a persistência. Ainda falta garantir unicidade no repositório e idempotência em reprocessamentos:

- [x] gerar `transactionId` no momento da criação do pagamento
- [x] persistir de forma explícita no documento
- [x] retorná-lo ao cliente em respostas de consulta
- [ ] validar unicidade do identificador

## 5) Testes de integração

O projeto precisa evoluir com cobertura de:

- integração com MongoDB em ambiente de teste
- integração com Kafka e MongoDB em ambiente isolado
- testes de contrato da API
- testes de concorrência e idempotência em transações financeiras

## 6) Segurança de rede e infraestrutura

- usar versões fixas de imagens Docker
- não expor serviços sensíveis em ambiente público sem controle
- manter credenciais em secret manager
- aplicar políticas de rede e TLS
- monitorar filas, consumo e falhas operacionais

## 7) Checklist de segurança mínimo

Antes de produção, o time deve confirmar:

- [ ] validação de entrada com Bean Validation
- [ ] autenticação para endpoints críticos
- [ ] mascaramento de dados sensíveis
- [x] `transactionId` gerado e persistido corretamente
- [ ] unicidade e idempotência de `transactionId`
- [ ] testes automatizados de integração
- [ ] configuração por ambiente sem segredos em código
- [ ] observabilidade e alertas

## Conclusão

O sistema apresenta um bom ponto de partida funcional, mas precisa de disciplina de segurança e validação para atender requisitos de produção. A proteção de dados, autenticação, prevenção de entrada inválida e rastreabilidade de transações são as prioridades mais importantes.
