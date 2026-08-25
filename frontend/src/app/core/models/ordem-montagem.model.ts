export type OrigemOrdemMontagem = 'ERP' | 'AVULSA';

export type StatusOrdemMontagem =
  | 'CRIADA'
  | 'AGENDADA'
  | 'EM_ANDAMENTO'
  | 'CONCLUIDA'
  | 'COM_PENDENCIA'
  | 'CANCELADA';

export interface ItemMontagem {
  id?: number;
  produtoIdExterno?: string;
  descricao: string;
  quantidade: number;
}

export interface OrdemMontagem {
  id: number;
  origem: OrigemOrdemMontagem;
  status: StatusOrdemMontagem;
  pedidoVendaIdExterno?: string;
  chaveNotaFiscal?: string;
  empresaIdExterno?: string;
  empresaNome?: string;
  filialIdExterno?: string;
  filialNome?: string;
  clienteIdExterno?: string;
  clienteNome: string;
  clienteTelefone?: string;
  enderecoEntrega: string;
  prazoCombinado?: string;
  observacoes?: string;
  valorServicoAvulso?: number;
  itens: ItemMontagem[];
  criadaEm: string;
  atualizadaEm: string;
}

export interface OrdemMontagemAvulsaPayload {
  clienteNome: string;
  clienteTelefone?: string;
  enderecoEntrega: string;
  itens: ItemMontagem[];
  prazoCombinado?: string;
  observacoes?: string;
  valorServicoAvulso?: number;
  empresaIdExterno?: string;
  empresaNome?: string;
  filialIdExterno?: string;
  filialNome?: string;
}
