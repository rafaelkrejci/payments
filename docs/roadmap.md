# Roadmap de evolução

## Fase 1 — Base operacional

### Objetivos

- estabilizar a aplicação em ambiente local
- validar o fluxo completo de mensagem e persistência
- padronizar configuração e documentação

### Atividades

- revisar `application.yaml` e separar perfis de ambiente
- documentar endpoints e exemplos de uso
- revisar dependências e versões do projeto
- verificar health checks e observabilidade inicial

## Fase 2 — Validação e contrato

### Objetivos

- tornar a API robusta e previsível
- reduzir erros de entrada e inconsistência de dados

### Atividades

- adicionar Bean Validation aos DTOs
- definir contratos de request/response explícitos
- criar mensagens de erro padronizadas
- validar tipos e formatos de dados sensíveis

## Fase 3 — Segurança

### Objetivos

- proteger a API e os dados processados
- reduzir riscos operacionais e legais

### Atividades

- implementar autenticação e autorização
- mascarar dados do cartão antes de retornar à API
- revisar logs e rastreabilidade
- reforçar o uso de segredos via secret manager

## Fase 4 — Integridade de negócio

### Objetivos

- melhorar confiabilidade da transação
- garantir rastreabilidade

### Atividades

- [x] gerar e persistir `transactionId`
- incluir idempotência para operações críticas
- validar unicidade de transações repetidas
- criar mecanismo de compensação ou reprocessamento

## Fase 5 — Qualidade e testes

### Objetivos

- garantir confiabilidade contínua
- reduzir regressões

### Atividades

- criar teste de integração com MongoDB
- criar teste de integração com Kafka
- incluir testes de API com MockMvc
- configurar CI/CD com execução de testes automáticos

## Fase 6 — Evolução arquitetural

### Objetivos

- organização mais limpa e sustentável
- escalabilidade do domínio

### Atividades

- [x] separar responsabilidades por packages
- [x] criar contrato explícito para mensagem Kafka
- [x] encapsular casos de uso em serviço de aplicação
- [x] definir portas e adaptadores para REST, Kafka e MongoDB

## Priorização recomendada

1. Validação de entrada
2. Segurança e autenticação
3. Persistência consistente de `transactionId`
4. Testes de integração
5. Perfis de ambiente
6. Organização modular
7. Monitoramento e operação

## Resumo executivo

O projeto já está em um estágio promissor como prova de conceito. A próxima etapa principal é transformar a aplicação em uma solução mais segura, testável e adequada para produção, sem perder a simplicidade e clareza da arquitetura atual.
