import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { OrdemMontagemService } from '../../core/services/ordem-montagem.service';
import { OrdemMontagem } from '../../core/models/ordem-montagem.model';

@Component({
  selector: 'app-ordens-montagem',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './ordens-montagem.component.html'
})
export class OrdensMontagemComponent implements OnInit {

  ordens: OrdemMontagem[] = [];
  carregando = true;

  constructor(private ordemMontagemService: OrdemMontagemService) {}

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
