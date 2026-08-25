import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { OrdemMontagemService } from '../../core/services/ordem-montagem.service';
import { OrdemMontagem } from '../../core/models/ordem-montagem.model';

@Component({
  selector: 'app-ordens-montagem',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './ordens-montagem.component.html'
})
export class OrdensMontagemComponent implements OnInit {

  private ordemMontagemService = inject(OrdemMontagemService);

  ordens: OrdemMontagem[] = [];
  carregando = true;
  itensPorPagina = 20;

  filtroCliente = '';
  filtroStatus = '';

  get listaFiltrada(): OrdemMontagem[] {
    return this.ordens.filter((o) =>
      o.clienteNome.toLowerCase().includes(this.filtroCliente.toLowerCase()) &&
      o.status.toLowerCase().includes(this.filtroStatus.toLowerCase())
    );
  }

  ngOnInit(): void {
    this.ordemMontagemService.listar().subscribe({
      next: (ordens) => {
        this.ordens = ordens;
        this.carregando = false;
      },
      error: () => this.carregando = false
    });
  }
}
