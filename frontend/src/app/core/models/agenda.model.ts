export type StatusAgenda = 'AGENDADO' | 'EM_ANDAMENTO' | 'CONCLUIDO' | 'REAGENDADO' | 'CANCELADO';

export interface Agenda {
  id?: number;
  ordemMontagemId: number;
  montadorId: number;
  dataHora: string;
  status?: StatusAgenda;
  motivoReagendamento?: string;
}
