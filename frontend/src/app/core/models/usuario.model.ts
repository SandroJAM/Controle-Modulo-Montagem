export type PerfilUsuarioMontagem = 'ADMIN' | 'GESTOR' | 'ATENDENTE' | 'MONTADOR';

export interface Usuario {
  id?: number;
  nome: string;
  email: string;
  senha?: string;
  perfil: PerfilUsuarioMontagem;
  ativo: boolean;
}
