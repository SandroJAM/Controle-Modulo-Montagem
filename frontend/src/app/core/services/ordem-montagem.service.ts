import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { OrdemMontagem, OrdemMontagemAvulsaPayload } from '../models/ordem-montagem.model';

@Injectable({ providedIn: 'root' })
export class OrdemMontagemService {

  private readonly baseUrl = `${environment.apiUrl}/ordens-montagem`;

  constructor(private http: HttpClient) {}

  listar(): Observable<OrdemMontagem[]> {
    return this.http.get<OrdemMontagem[]>(this.baseUrl);
  }

  buscarPorId(id: number): Observable<OrdemMontagem> {
    return this.http.get<OrdemMontagem>(`${this.baseUrl}/${id}`);
  }

  criarAvulsa(payload: OrdemMontagemAvulsaPayload): Observable<OrdemMontagem> {
    return this.http.post<OrdemMontagem>(`${this.baseUrl}/avulsa`, payload);
  }

  atualizar(id: number, payload: Partial<OrdemMontagem>): Observable<OrdemMontagem> {
    return this.http.patch<OrdemMontagem>(`${this.baseUrl}/${id}`, payload);
  }

  registrarChecklist(id: number, payload: unknown): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${id}/checklist`, payload);
  }

  registrarOcorrencia(id: number, payload: unknown): Observable<unknown> {
    return this.http.post(`${this.baseUrl}/${id}/ocorrencias`, payload);
  }
}
