import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MontadorService } from '../../core/services/montador.service';
import { Montador } from '../../core/models/montador.model';

@Component({
  selector: 'app-montadores',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterLink],
  templateUrl: './montadores.component.html'
})
export class MontadoresComponent implements OnInit {

  private fb = inject(FormBuilder);
  private montadorService = inject(MontadorService);

  montadores: Montador[] = [];
  itensPorPagina = 20;

  mostrarFormulario = signal(false);
  idEmEdicao: number | null = null;
  selecionadoId = signal<number | null>(null);

  filtroNome = '';
  filtroArea = '';

  form = this.fb.group({
    nome: ['', Validators.required],
    telefone: [''],
    areaAtuacao: [''],
    terceirizado: [false]
  });

  get listaFiltrada(): Montador[] {
    return this.montadores.filter((m) =>
      m.nome.toLowerCase().includes(this.filtroNome.toLowerCase()) &&
      (m.areaAtuacao ?? '').toLowerCase().includes(this.filtroArea.toLowerCase())
    );
  }

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.montadorService.listarAtivos().subscribe((montadores) => this.montadores = montadores);
  }

  abrirInserir(): void {
    this.idEmEdicao = null;
    this.form.reset({ terceirizado: false });
    this.mostrarFormulario.set(true);
  }

  abrirEditar(): void {
    const id = this.selecionadoId();
    const montador = this.montadores.find((m) => m.id === id);
    if (!montador) return;

    this.idEmEdicao = montador.id!;
    this.form.reset({
      nome: montador.nome,
      telefone: montador.telefone,
      areaAtuacao: montador.areaAtuacao,
      terceirizado: montador.terceirizado
    });
    this.mostrarFormulario.set(true);
  }

  cancelar(): void {
    this.mostrarFormulario.set(false);
    this.idEmEdicao = null;
  }

  selecionar(id: number): void {
    this.selecionadoId.set(this.selecionadoId() === id ? null : id);
  }

  submeter(): void {
    if (this.form.invalid) {
      return;
    }

    const dados = { ...this.form.getRawValue(), ativo: true } as Montador;

    const operacao = this.idEmEdicao
      ? this.montadorService.atualizar(this.idEmEdicao, dados)
      : this.montadorService.criar(dados);

    operacao.subscribe(() => {
      this.mostrarFormulario.set(false);
      this.idEmEdicao = null;
      this.selecionadoId.set(null);
      this.carregar();
    });
  }
}
