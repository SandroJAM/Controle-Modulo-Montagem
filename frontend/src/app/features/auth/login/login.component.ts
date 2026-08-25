import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

const EMAIL_LEMBRADO_KEY = 'modulo_montagem_email_lembrado';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  erro: string | null = null;
  carregando = false;

  form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    senha: ['', Validators.required],
    lembrarEmail: [true]
  });

  ngOnInit(): void {
    const emailLembrado = localStorage.getItem(EMAIL_LEMBRADO_KEY);
    if (emailLembrado) {
      this.form.patchValue({ email: emailLembrado });
    }
  }

  submeter(): void {
    if (this.form.invalid) {
      return;
    }

    this.erro = null;
    this.carregando = true;

    const { email, senha, lembrarEmail } = this.form.getRawValue();

    this.authService.login({ email: email!, senha: senha! }).subscribe({
      next: () => {
        if (lembrarEmail) {
          localStorage.setItem(EMAIL_LEMBRADO_KEY, email!);
        } else {
          localStorage.removeItem(EMAIL_LEMBRADO_KEY);
        }
        this.router.navigate(['/ordens-montagem']);
      },
      error: () => {
        this.carregando = false;
        this.erro = 'E-mail ou senha inválidos.';
      }
    });
  }
}
