import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { UsuarioService } from '../../core/services/usuario.service';
import { Usuario } from '../../core/models/usuario.model';

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, RouterLink],
  templateUrl: './usuarios.component.html'
})
export class UsuariosComponent implements OnInit {

  private fb = inject(FormBuilder);
  private usuarioService = inject(UsuarioService);

  usuarios: Usuario[] = [];
  perfis = ['ADMIN', 'GESTOR', 'ATENDENTE', 'MONTADOR'];
  itensPorPagina = 20;

  mostrarFormulario = signal(false);
  idEmEdicao: number | null = null;
  selecionadoId = signal<number | null>(null);

  filtroNome = '';
  filtroEmail = '';

  form = this.fb.group({
    nome: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    senha: [''],
    perfil: ['ATENDENTE', Validators.required]
  });

  get listaFiltrada(): Usuario[] {
    return this.usuarios.filter((u) =>
      u.nome.toLowerCase().includes(this.filtroNome.toLowerCase()) &&
      u.email.toLowerCase().includes(this.filtroEmail.toLowerCase())
    );
  }

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.usuarioService.listar().subscribe((usuarios) => this.usuarios = usuarios);
  }

  abrirInserir(): void {
    this.idEmEdicao = null;
    this.form.reset({ perfil: 'ATENDENTE' });
    this.form.get('senha')?.setValidators(Validators.required);
    this.form.get('senha')?.updateValueAndValidity();
    this.mostrarFormulario.set(true);
  }

  abrirEditar(): void {
    const id = this.selecionadoId();
    const usuario = this.usuarios.find((u) => u.id === id);
    if (!usuario) return;

    this.idEmEdicao = usuario.id!;
    // Na edicao a senha e opcional: so troca se o campo for preenchido.
    this.form.get('senha')?.clearValidators();
    this.form.get('senha')?.updateValueAndValidity();
    this.form.reset({
      nome: usuario.nome,
      email: usuario.email,
      senha: '',
      perfil: usuario.perfil
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

    const dados = { ...this.form.getRawValue(), ativo: true } as Usuario;

    const operacao = this.idEmEdicao
      ? this.usuarioService.atualizar(this.idEmEdicao, dados)
      : this.usuarioService.criar(dados);

    operacao.subscribe(() => {
      this.mostrarFormulario.set(false);
      this.idEmEdicao = null;
      this.selecionadoId.set(null);
      this.carregar();
    });
  }
}
