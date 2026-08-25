import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RelatorioService } from '../../core/services/relatorio.service';
import { VolumePorFilial } from '../../core/models/relatorio.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {

  dados: VolumePorFilial[] = [];
  carregando = true;

  origem: 'ERP' | 'AVULSA' = 'ERP';
  dataInicio = this.formatarData(this.subtrairDias(new Date(), 30));
  dataFim = this.formatarData(new Date());

  get totalGeral(): number {
    return this.dados.reduce((soma, d) => soma + d.totalOrdens, 0);
  }

  get maiorVolume(): number {
    return Math.max(1, ...this.dados.map(d => d.totalOrdens));
  }

  constructor(private relatorioService: RelatorioService) {}

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.carregando = true;
    this.relatorioService.volumePorFilial(this.dataInicio, this.dataFim, this.origem).subscribe({
      next: (dados) => {
        this.dados = dados;
        this.carregando = false;
      },
      error: () => this.carregando = false
    });
  }

  private subtrairDias(data: Date, dias: number): Date {
    const nova = new Date(data);
    nova.setDate(nova.getDate() - dias);
    return nova;
  }

  private formatarData(data: Date): string {
    return data.toISOString().substring(0, 10);
  }
}
