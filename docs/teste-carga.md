# Teste de carga

O teste usa [k6](https://k6.io/) para enviar requisições `POST` ao endpoint
`/api/v1/payments/`. O cenário padrão começa em 5.000 requisições por segundo,
mantém essa taxa por 1 minuto, aumenta gradualmente até 20.000 req/s em 1 minuto
e mantém a taxa final por mais 1 minuto.

## Pré-requisitos

- k6 instalado na máquina que executará o teste;
- aplicação e Kafka disponíveis e acessíveis;
- executar preferencialmente contra um ambiente de teste isolado, nunca contra
  produção sem autorização e planejamento.

## Execução

Com a aplicação disponível em `http://localhost:8080`:

```bash
k6 run load-tests/payments.js
```

É possível definir a URL e as taxas, por exemplo:

```bash
BASE_URL=http://localhost:8080 START_RATE=5000 END_RATE=20000 MAX_VUS=1000 k6 run load-tests/payments.js
```

As variáveis `HOLD_DURATION` e `RAMP_UP_DURATION` aceitam durações do k6
(padrão `1m`). `PREALLOCATED_VUS` e `MAX_VUS` ajustam os VUs disponíveis ao
gerador (padrões `1000` e `5000`); aumente-os apenas se a máquina de carga tiver
capacidade suficiente, e `PREALLOCATED_VUS` não pode exceder `MAX_VUS`. O teste
falha se mais de 1% das requisições falharem, se menos de 99% das verificações
passarem ou se o k6 descartar alguma iteração por falta de VUs. Cada resposta
também precisa conter um `transactionId` não vazio.

Na configuração local, o Tomcat usa `accept-count` e `max-connections` de 20.000;
o limite de threads não está definido explicitamente e depende do padrão do
Spring Boot/Tomcat. Esses valores não representam uma meta universal: compare o
p95 junto com CPU, threads ocupadas e erros em cada execução e ajuste a
configuração do servidor somente com base nas métricas observadas.

O payload usa apenas dados sintéticos. Cada iteração mede a aceitação HTTP e a
publicação no Kafka; isso não garante que o consumidor tenha persistido os
pagamentos no MongoDB. Monitore também CPU, memória, conexões, lag do consumidor
Kafka, erros e persistência. O volume de mensagens pode crescer se os consumidores
não acompanharem a taxa de publicação.
