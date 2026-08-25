import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Montador } from '../models/montador.model';

@Injectable({ providedIn: 'root' })
export class MontadorService {

  private readonly baseUrl = `${environment.apiUrl}/montadores`;

  constructor(private http: HttpClient) {}

  listarAtivos(): Observable<Montador[]> {
    return this.http.get<Montador[]>(this.baseUrl);
  }

  criar(montador: Montador): Observable<Montador> {
    return this.http.post<Montador>(this.baseUrl, montador);
  }

  atualizar(id: number, montador: Montador): Observable<Montador> {
    return this.http.patch<Montador>(`${this.baseUrl}/${id}`, montador);
  }
}
