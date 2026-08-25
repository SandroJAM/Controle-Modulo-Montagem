import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequest, LoginResponse } from '../models/auth.model';

const TOKEN_KEY = 'modulo_montagem_token';
const PERFIL_KEY = 'modulo_montagem_perfil';
const NOME_KEY = 'modulo_montagem_nome';

@Injectable({ providedIn: 'root' })
export class AuthService {

  perfilAtual = signal<string | null>(sessionStorage.getItem(PERFIL_KEY));
  nomeAtual = signal<string | null>(sessionStorage.getItem(NOME_KEY));

  constructor(private http: HttpClient) {}

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${environment.apiUrl}/auth/login`, request).pipe(
      tap((res) => {
        sessionStorage.setItem(TOKEN_KEY, res.token);
        sessionStorage.setItem(PERFIL_KEY, res.perfil);
        sessionStorage.setItem(NOME_KEY, res.nome);
        this.perfilAtual.set(res.perfil);
        this.nomeAtual.set(res.nome);
      })
    );
  }

  logout(): void {
    sessionStorage.clear();
    this.perfilAtual.set(null);
    this.nomeAtual.set(null);
  }

  getToken(): string | null {
    return sessionStorage.getItem(TOKEN_KEY);
  }

  isAutenticado(): boolean {
    return !!this.getToken();
  }

  isAdmin(): boolean {
    return this.perfilAtual() === 'ADMIN';
  }

  podeVerRelatorios(): boolean {
    return this.perfilAtual() === 'ADMIN' || this.perfilAtual() === 'GESTOR';
  }
}
