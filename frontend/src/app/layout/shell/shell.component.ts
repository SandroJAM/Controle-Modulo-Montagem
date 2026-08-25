import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

interface ItemMenu {
  label: string;
  path: string;
  icone: string; // nome do icone, ver iconePath()
}

interface GrupoMenu {
  label: string;
  icone: string;
  itens: ItemMenu[];
}

@Component({
  selector: 'app-shell',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './shell.component.html',
  styleUrls: ['./shell.component.scss']
})
export class ShellComponent {

  private authService = inject(AuthService);
  private router = inject(Router);

  colapsado = signal(false);
  gruposAbertos = signal<Record<string, boolean>>({
    ordens: true,
    operacao: true,
    administracao: true
  });

  get nomeUsuario(): string | null {
    return this.authService.nomeAtual();
  }

  get perfilUsuario(): string | null {
    return this.authService.perfilAtual();
  }

  get podeVerRelatorios(): boolean {
    return this.authService.podeVerRelatorios();
  }

  get isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  grupos: GrupoMenu[] = [
    {
      label: 'Ordens de Montagem',
      icone: 'clipboard',
      itens: [
        { label: 'Todas as Ordens', path: '/ordens-montagem', icone: 'list' },
        { label: 'Nova Ordem Avulsa', path: '/ordens-montagem/nova-avulsa', icone: 'plus' }
      ]
    },
    {
      label: 'Operação',
      icone: 'wrench',
      itens: [
        { label: 'Agenda', path: '/agenda', icone: 'calendar' },
        { label: 'Montadores', path: '/montadores', icone: 'users' }
      ]
    }
  ];

  toggleColapsado(): void {
    this.colapsado.update((v) => !v);
  }

  toggleGrupo(chave: string): void {
    this.gruposAbertos.update((atual) => ({ ...atual, [chave]: !atual[chave] }));
  }

  grupoAberto(chave: string): boolean {
    return !!this.gruposAbertos()[chave];
  }

  chaveGrupo(label: string): string {
    if (label === 'Ordens de Montagem') return 'ordens';
    if (label === 'Operação') return 'operacao';
    return 'administracao';
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
