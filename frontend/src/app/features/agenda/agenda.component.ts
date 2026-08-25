import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AgendaService } from '../../core/services/agenda.service';
import { Agenda } from '../../core/models/agenda.model';

@Component({
  selector: 'app-agenda',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterLink],
  templateUrl: './agenda.component.html'
})
export class AgendaComponent implements OnInit {

  private fb = inject(FormBuilder);
  private agendaService = inject(AgendaService);

  agendamentos: Agenda[] = [];
  carregando = true;
  itensPorPagina = 20;

  mostrarFormulario = signal(false);
  confirmacao: string | null = null;

  filtroOrdem = '';
  filtroMontador = '';

  form = this.fb.group({
    ordemMontagemId: [null as number | null, Validators.required],
    montadorId: [null as number | null, Validators.required],
    dataHora: ['', Validators.required]
  });

  get listaFiltrada(): Agenda[] {
    return this.agendamentos.filter((a) =>
      String(a.ordemMontagemId).includes(this.filtroOrdem) &&
      String(a.montadorId).includes(this.filtroMontador)
    );
  }

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.carregando = true;
    this.agendaService.listarTodas().subscribe({
      next: (agendamentos) => {
        this.agendamentos = agendamentos;
        this.carregando = false;
      },
      error: () => this.carregando = false
    });
  }

  abrirInserir(): void {
    this.form.reset();
    this.confirmacao = null;
    this.mostrarFormulario.set(true);
  }

  cancelar(): void {
    this.mostrarFormulario.set(false);
  }

  submeter(): void {
    if (this.form.invalid) {
      return;
    }

    this.agendaService.agendar(this.form.getRawValue() as any).subscribe(() => {
      this.confirmacao = 'Montagem agendada com sucesso.';
      this.mostrarFormulario.set(false);
      this.carregar();
    });
  }
}
