import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { VolumePorFilial } from '../models/relatorio.model';

@Injectable({ providedIn: 'root' })
export class RelatorioService {

  private readonly baseUrl = `${environment.apiUrl}/relatorios`;

  constructor(private http: HttpClient) {}

  volumePorFilial(dataInicio?: string, dataFim?: string, origem?: 'ERP' | 'AVULSA'): Observable<VolumePorFilial[]> {
    const params: Record<string, string> = {};
    if (dataInicio) params['dataInicio'] = dataInicio;
    if (dataFim) params['dataFim'] = dataFim;
    if (origem) params['origem'] = origem;

    return this.http.get<VolumePorFilial[]>(`${this.baseUrl}/volume-montagens-por-filial`, { params });
  }
}
