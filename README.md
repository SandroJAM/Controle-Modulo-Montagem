# Módulo de Montagem de Móveis

Módulo independente de gerenciamento de montagem de móveis, integrável a um
sistema ERP via API. Não substitui o ERP: cadastro de cliente, pedido de
venda e nota fiscal continuam sendo responsabilidade dele. Este módulo cuida
apenas do processo operacional de montagem, do agendamento à conclusão.

## Stack

- **Backend:** Java 17 + Spring Boot 3 + PostgreSQL + Spring Security (JWT)
- **Frontend:** Angular 18 (standalone components)

Mesmo padrão tecnológico do sistema ERP, para facilitar manutenção pela
mesma equipe.

## Como o módulo recebe trabalho

1. **Via ERP (automático):** quando uma nota fiscal é emitida para um pedido
   com item de montagem, o ERP chama `POST /api/webhooks/pedido-venda`
   (autenticado por `X-Api-Key`) e o módulo cria a Ordem de Montagem.
2. **Avulsa (manual):** um usuário do módulo cria a ordem diretamente pela
   tela, sem vínculo com o ERP. Hoje roda fora do financeiro (sem geração de
   cobrança/nota fiscal).

Veja o fluxo completo e o modelo de dados em [`docs/modelo-dados.md`](docs/modelo-dados.md).

## Rodando o backend

```bash
cd backend
cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
# edite src/main/resources/application-local.properties com suas credenciais do Postgres
createdb modulo_montagem
mvn spring-boot:run
```

A API sobe em `http://localhost:8081/api`.

### Variáveis de ambiente relevantes

| Variável | Descrição |
|---|---|
| `DB_PASSWORD` | senha do Postgres |
| `ERP_WEBHOOK_API_KEY` | chave que o ERP deve enviar no header `X-Api-Key` |
| `ERP_NOTIFICACAO_URL` | URL do ERP para onde o módulo envia atualizações de status |
| `ADMIN_INICIAL_NOME` | nome do usuário admin criado automaticamente (opcional) |
| `ADMIN_INICIAL_EMAIL` | e-mail do usuário admin criado automaticamente (opcional) |
| `ADMIN_INICIAL_SENHA` | senha do usuário admin criado automaticamente (defina antes do primeiro `run`!) |

## Primeiro acesso

Na primeira vez que o backend sobe, se o banco não tiver **nenhum** usuário
ainda, um usuário `ADMIN` é criado automaticamente (ver `AdminSeeder`).
Por padrão as credenciais são `admin@modulomontagem.local` / `troque-esta-senha`
— **defina `ADMIN_INICIAL_EMAIL`/`ADMIN_INICIAL_SENHA` antes de rodar pela
primeira vez** para não depender do valor padrão, especialmente fora do seu
ambiente local. Depois de logar, crie seu usuário definitivo pela tela de
Usuários e, se quiser, desative este.

## Rodando o frontend

```bash
cd frontend
npm install
npm start
```

Sobe em `http://localhost:4201`, apontando para a API em `http://localhost:8081/api`
(ver `src/environments/environment.ts`).

## Perfis de acesso

| Perfil | Pode fazer |
|---|---|
| `ADMIN` | Tudo, inclusive gerenciar usuários |
| `GESTOR` | Cria/edita ordens (inclusive avulsas), atribui montadores, agenda |
| `ATENDENTE` | Cria ordens avulsas, consulta status |
| `MONTADOR` | Vê e atualiza apenas as ordens atribuídas a ele (checklist, ocorrências) |

## Próximos passos sugeridos

- [ ] Adicionar Flyway para migrations versionadas (hoje usa `ddl-auto=update`,
      adequado só para desenvolvimento).
- [ ] Tela de detalhe da Ordem de Montagem (checklist, ocorrências, histórico).
- [ ] Sugestão de montador por área de atuação/disponibilidade na tela de agenda.
- [ ] Testes automatizados (backend: JUnit + MockMvc; frontend: Jasmine/Karma).
- [ ] Fila de mensagens (RabbitMQ/SQS) para as notificações ao ERP, se o
      volume de eventos crescer e a chamada síncrona virar gargalo.
