import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Agenda } from '../models/agenda.model';

@Injectable({ providedIn: 'root' })
export class AgendaService {

  private readonly baseUrl = `${environment.apiUrl}/agenda`;

  constructor(private http: HttpClient) {}

  agendar(agenda: Agenda): Observable<Agenda> {
    return this.http.post<Agenda>(this.baseUrl, agenda);
  }

  listarTodas(): Observable<Agenda[]> {
    return this.http.get<Agenda[]>(this.baseUrl);
  }

  listarPorOrdem(ordemMontagemId: number): Observable<Agenda[]> {
    return this.http.get<Agenda[]>(this.baseUrl, { params: { ordemMontagemId } });
  }
}
