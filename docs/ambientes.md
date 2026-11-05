# Ambientes e configurações

## Perfis de ambiente

O projeto deve evoluir para separar as configurações de cada ambiente:

- `dev`
- `test`
- `prod`

A estrutura mais adequada é a utilização de arquivos específicos do Spring Boot, por exemplo:

```text
src/main/resources/
  application.yml
  application-dev.yml
  application-test.yml
  application-prod.yml
```

## Configuração atual

Hoje o projeto usa um único arquivo de configuração:

```yaml
spring:
  application:
    name: payments
  mongodb:
    authentication-database: admin
    host: localhost
    port: 27017
    database: paymentsDb
    username: root
    password: secret
```

## Recomendações para organização

### Desenvolvimento

```yaml
spring:
  config:
    activate:
      on-profile: dev
  mongodb:
    host: localhost
    port: 27017
    database: paymentsDb
    username: root
    password: secret
```

### Teste

```yaml
spring:
  config:
    activate:
      on-profile: test
  mongodb:
    host: localhost
    port: 27017
    database: payments_test
    username: root
    password: secret
```

### Produção

```yaml
spring:
  config:
    activate:
      on-profile: prod
  mongodb:
    host: ${MONGODB_HOST}
    port: ${MONGODB_PORT:27017}
    database: ${MONGODB_DATABASE}
    username: ${MONGODB_USERNAME}
    password: ${MONGODB_PASSWORD}
```

## Variáveis de ambiente sugeridas

```bash
export SPRING_PROFILES_ACTIVE=prod
export MONGODB_HOST=payments-db
export MONGODB_PORT=27017
export MONGODB_DATABASE=paymentsDb
export MONGODB_USERNAME=payments_user
export MONGODB_PASSWORD=***
export SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:29092
```

## Docker Compose

O projeto possui dois arquivos de orquestração:

- `docker-compose.yml`: infraestrutura base para MongoDB, Kafka, Kafka UI e collector.
- `docker-compose.override.yml`: contém uma composição antiga comentada; a infraestrutura ativa está em `docker-compose.yml`.

Uso recomendado:

```bash
docker compose -f docker-compose.yml -f docker-compose.override.yml up --build
```

Em produção, o ideal é:

- trocar imagens `latest` por versões fixas
- usar redes internas isoladas
- separar bancos por ambiente
- não armazenar credenciais sensíveis em arquivos versionados

## Boas práticas

- usar secret management (Kubernetes Secrets, Docker secrets, Vault, AWS Secrets Manager, etc.)
- impedir uso de senhas em texto puro no código
- separar `application.yml` de segredos e variáveis de runtime
- configurar logs e métricas por ambiente
- registrar health checks e alertas para infra dependente

## Execução por ambiente

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
./mvnw test -Dspring-boot.run.profiles=test
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

## Observação

O projeto já possui infraestrutura adequada para desenvolvimento local, mas ainda precisa de refinamento para ambientes reais de entrega, principalmente no que diz respeito a segurança, replicação, segredos e parametrização.
