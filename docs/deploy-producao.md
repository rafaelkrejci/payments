# Guia de implantação em produção

Este guia descreve os controles e etapas recomendados para implantar o `payments` em produção. A configuração atual do repositório é voltada ao desenvolvimento local e **não deve ser publicada diretamente em produção**.

## Estado atual e bloqueios antes da produção

Antes de disponibilizar o serviço para tráfego real, resolva ou aceite formalmente os itens abaixo:

- Credenciais de MongoDB de desenvolvimento estão declaradas em arquivos versionados. Substitua-as e injete segredos pelo mecanismo de secrets da plataforma.
- O Compose local inclui Mongo Express, expõe portas de banco e broker e usa imagens `latest`. Não use essa composição diretamente em produção.
- A API não configura autenticação ou autorização e expõe campos de cartão, incluindo PAN e CVV, nos contratos de entrada/saída e em logs. Não processe dados reais de cartão até revisar a solução com segurança e conformidade aplicáveis.
- O caso de uso gera `transactionId` antes de publicar o evento, mas unicidade/idempotência ainda não estão garantidas.
- O Dockerfile não configura usuário não privilegiado nem health check de container. Revise esses pontos para a plataforma de destino.
- O `depends_on` do Compose local só estabelece ordem de inicialização; não garante que MongoDB ou Kafka estejam prontos para aceitar conexões.

## 1. Preparar a configuração

Use configurações independentes para aplicação, MongoDB e Kafka. Não coloque senhas, tokens ou certificados no repositório, na imagem ou em comandos que possam ficar no histórico do terminal.

A aplicação declara configurações MongoDB no namespace `spring.mongodb` em `src/main/resources/application.yaml`. O Compose de desenvolvimento define variáveis `SPRING_DATA_MONGODB_*`; confirme que as propriedades de runtime são mapeadas como esperado na versão do Spring Boot usada antes de reaproveitar essa configuração. Para produção, teste a configuração efetiva com os valores fornecidos pela plataforma.

As propriedades Kafka devem apontar para brokers acessíveis pela rede privada do serviço. Armazene credenciais em secrets, habilite TLS/autenticação quando suportados pela infraestrutura e restrinja o acesso a clientes autorizados.

Checklist de configuração:

- [ ] definir profile e propriedades explícitas para produção
- [ ] apontar MongoDB e Kafka para endpoints privados e persistentes
- [ ] usar credenciais dedicadas, com privilégios mínimos e rotação
- [ ] armazenar segredos fora do Git e da imagem
- [ ] não expor Mongo Express em produção
- [ ] restringir acesso de rede à API, ao banco e ao broker
- [ ] definir limites de CPU/memória, réplicas e política de reinício na plataforma
- [ ] confirmar o comportamento de inicialização quando uma dependência está indisponível

## 2. Construir e publicar a imagem

Execute testes antes de gerar a imagem:

```bash
./mvnw clean verify
```

Construa a imagem com uma tag imutável associada à versão ou ao commit:

```bash
docker build -t <registry>/<namespace>/payments:<versao> .
docker push <registry>/<namespace>/payments:<versao>
```

Não use `latest` como identificador de release. Registre a tag e, se disponível, o digest da imagem implantada. A imagem é construída com Java 25; a plataforma de execução deve suportar essa versão.

## 3. Preparar serviços de apoio

MongoDB e Kafka devem ser serviços gerenciados ou implantados com persistência, backups e monitoramento adequados. Antes do deploy:

1. Crie banco, usuário e permissões mínimos para a aplicação.
2. Configure backups e valide um procedimento de restauração do MongoDB.
3. Configure replicação, capacidade, retenção e alertas apropriados para Kafka.
4. Restrinja portas de administração e acesso aos brokers à rede operacional.
5. Confirme conectividade de rede e resolução DNS a partir do ambiente da aplicação.

## 4. Implantar a aplicação

Implante a imagem publicada na plataforma escolhida e injete configuração e secrets em runtime. Mantenha a aplicação em rede privada para suas dependências e exponha a API somente através de um ingress, load balancer ou gateway com TLS e políticas de acesso apropriadas.

Configure verificações de saúde para `/actuator/health` somente depois de confirmar que o endpoint está habilitado, acessível e adequado à política de exposição da plataforma. Configure readiness para impedir tráfego antes de a aplicação estar pronta e liveness para detectar processos travados; não use uma falha transitória de dependência como sinal para reiniciar continuamente todas as réplicas.

Faça uma implantação gradual quando a plataforma oferecer suporte a rolling update ou canary. Garanta que a versão anterior e a nova possam coexistir com o formato dos documentos armazenados e com a topologia das filas.

## 5. Validar após a implantação

- [ ] confirmar que a nova versão iniciou sem erros de configuração
- [ ] confirmar health/readiness na plataforma
- [ ] verificar conectividade e autenticação com MongoDB e Kafka
- [ ] chamar os endpoints de leitura e criação com dados de teste não sensíveis
- [ ] confirmar publicação, consumo e persistência de uma mensagem
- [ ] verificar métricas, logs, filas acumuladas e alertas
- [ ] confirmar que logs e respostas não contêm PAN, CVV, credenciais ou outros dados sensíveis

Não use dados reais de cartão em smoke tests.

## 6. Monitoramento e operação

Monitore disponibilidade, latência e erros HTTP da API, falhas de conexão com dependências, mensagens rejeitadas/reprocessadas, tamanho e idade das filas, uso de recursos e espaço disponível no banco. Defina alertas com responsáveis e procedimentos de atendimento.

Proteja endpoints de Actuator e páginas administrativas. Exponha apenas os endpoints estritamente necessários e não publique detalhes internos para a internet.

## 7. Rollback

Mantenha identificadas a versão atual e a versão anterior da imagem. Se houver regressão:

1. interrompa a promoção de novas réplicas;
2. reverta a implantação para a tag/digest previamente validado;
3. verifique health, tráfego, consumo da fila e persistência;
4. avalie mensagens publicadas pela versão nova e compatibilidade de dados;
5. registre o incidente e só reaplique a release após corrigir e validar a causa.

Rollback da aplicação não reverte automaticamente alterações no MongoDB nem mensagens já publicadas no Kafka. Mudanças de schema e contrato de mensagens devem ser compatíveis com a versão anterior ou possuir plano explícito de recuperação.

## Referências do projeto

- [Ambientes e configurações](ambientes.md)
- [Validação e segurança](seguranca-validacao.md)
- [Arquitetura](arquitetura.md)
