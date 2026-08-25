# Modelo de Dados — Módulo de Montagem

## Visão geral

O módulo **não duplica** dados comerciais do ERP (cliente completo, pedido de
venda, nota fiscal). Ele guarda apenas referências externas (`*IdExterno`,
`chaveNotaFiscal`) e os dados mínimos necessários para operar o processo de
montagem.

## Entidades

### Usuario
| Campo | Tipo | Observação |
|---|---|---|
| id | Long | PK |
| nome | String | |
| email | String | único, usado como login |
| senhaHash | String | BCrypt |
| perfil | Enum `PerfilUsuarioMontagem` | ADMIN, GESTOR, ATENDENTE, MONTADOR |
| ativo | boolean | |

### OrdemMontagem
| Campo | Tipo | Observação |
|---|---|---|
| id | Long | PK |
| origem | Enum `OrigemOrdemMontagem` | ERP ou AVULSA |
| status | Enum `StatusOrdemMontagem` | CRIADA, AGENDADA, EM_ANDAMENTO, CONCLUIDA, COM_PENDENCIA, CANCELADA |
| pedidoVendaIdExterno | String | nulo se origem = AVULSA |
| chaveNotaFiscal | String | gatilho de criação; nulo se AVULSA |
| empresaIdExterno / empresaNome | String | empresa (razão social do grupo) que emitiu a NF; obrigatório se origem = ERP |
| filialIdExterno / filialNome | String | filial/loja que emitiu a NF; obrigatório se origem = ERP; opcional em ordens avulsas |
| clienteIdExterno | String | nulo se AVULSA e cliente não cadastrado no ERP |
| clienteNome / clienteTelefone | String | cópia mínima para operação |
| enderecoEntrega | String | |
| prazoCombinado | LocalDate | |
| observacoes | String | |
| valorServicoAvulso | BigDecimal | preenchido só quando origem = AVULSA; roda fora do financeiro do ERP |
| itens | List\<ItemMontagem\> | |

### ItemMontagem
produtoIdExterno, descricao, quantidade — vinculado a uma OrdemMontagem.

### Montador
nome, telefone, areaAtuacao, terceirizado, ativo.

### Agenda
ordemMontagem, montador, dataHora, status (`StatusAgenda`), motivoReagendamento.

### ChecklistMontagem
1:1 com OrdemMontagem — pecasConferidas, montagemFinalizada, clienteConfirmou,
fotosUrls, observacoes.

### Ocorrencia
ordemMontagem, tipo (PECA_FALTANTE, AVARIA, REMARCACAO, OUTRO), descricao,
registradaEm.

## Fluxo de integração com o ERP

```
Venda fechada → Nota Fiscal emitida → ERP verifica se há item de montagem
                                            │ (se sim)
                                            ▼
                    POST /api/webhooks/pedido-venda  (header X-Api-Key)
                                            │
                                            ▼
                          Módulo cria OrdemMontagem (origem=ERP)
                                            │
                    a cada mudança de status ▼
                    Módulo → POST {app.erp.notificacao-url} (X-Api-Key)
```

Ordens **avulsas** (origem=AVULSA) são criadas via
`POST /api/ordens-montagem/avulsa`, direto no módulo, sem qualquer
referência a pedido/nota fiscal e sem notificar o ERP — hoje rodam fora do
financeiro. Empresa/filial são opcionais nesse caso; preencher apenas se a
filial que atendeu o avulso também precisar entrar nos relatórios
segmentados por empresa/filial.

## Consulta por empresa/filial

`GET /api/ordens-montagem?empresaId=&filialId=` retorna as ordens de uma
empresa/filial específica — útil para relatórios e para operações que
atendem múltiplas lojas do grupo.

## Dashboard: volume de montagens por filial

`GET /api/relatorios/volume-montagens-por-filial?dataInicio=&dataFim=&origem=`
(somente `ADMIN`/`GESTOR`) retorna, agrupado por empresa/filial, o total de
ordens no período e a quebra por status (concluídas, em andamento, com
pendência, canceladas). Todos os parâmetros são opcionais — sem eles,
considera os últimos 30 dias e `origem = ERP` (isto é, o volume ligado a
vendas reais). Passar `origem=AVULSA` mostra o volume de serviços avulsos
separadamente.
