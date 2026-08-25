import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {

  erro: string | null = null;

  form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    senha: ['', Validators.required]
  });

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  submeter(): void {
    if (this.form.invalid) {
      return;
    }

    this.erro = null;

    this.authService.login({
      email: this.form.value.email!,
      senha: this.form.value.senha!
    }).subscribe({
      next: () => this.router.navigate(['/ordens-montagem']),
      error: () => this.erro = 'E-mail ou senha invalidos.'
    });
  }
}
