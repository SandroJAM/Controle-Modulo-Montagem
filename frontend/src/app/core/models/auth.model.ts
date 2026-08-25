export interface LoginRequest {
  email: string;
  senha: string;
}

export interface LoginResponse {
  token: string;
  nome: string;
  perfil: 'ADMIN' | 'GESTOR' | 'ATENDENTE' | 'MONTADOR';
}
