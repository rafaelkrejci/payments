# Ajuda

Consulte o [README](README.md) para os pré-requisitos, execução local, endpoints e testes do projeto.

## Referências oficiais

- [Documentação do Spring Boot](https://docs.spring.io/spring-boot/)
- [Spring for Apache Kafka](https://docs.spring.io/spring-kafka/reference/)
- [Spring Data MongoDB](https://docs.spring.io/spring-data/mongodb/reference/)
- [Apache Kafka](https://kafka.apache.org/documentation/)

## Infraestrutura local

Os serviços de desenvolvimento são definidos em `docker-compose.yml` e `docker-compose.override.yml`. Para iniciá-los:

```bash
docker compose -f docker-compose.yml -f docker-compose.override.yml up -d
```

A aplicação deve ser iniciada separadamente com `./mvnw spring-boot:run`.
