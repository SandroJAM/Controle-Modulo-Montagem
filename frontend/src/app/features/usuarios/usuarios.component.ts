import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { UsuarioService } from '../../core/services/usuario.service';
import { Usuario } from '../../core/models/usuario.model';

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './usuarios.component.html'
})
export class UsuariosComponent implements OnInit {

  usuarios: Usuario[] = [];

  form = this.fb.group({
    nome: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    senha: ['', Validators.required],
    perfil: ['ATENDENTE', Validators.required]
  });

  perfis = ['ADMIN', 'GESTOR', 'ATENDENTE', 'MONTADOR'];

  constructor(private fb: FormBuilder, private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.usuarioService.listar().subscribe((usuarios) => this.usuarios = usuarios);
  }

  submeter(): void {
    if (this.form.invalid) {
      return;
    }

    this.usuarioService.criar({
      ...this.form.getRawValue(),
      ativo: true
    } as Usuario).subscribe(() => {
      this.form.reset({ perfil: 'ATENDENTE' });
      this.carregar();
    });
  }
}
