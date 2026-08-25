import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AgendaService } from '../../core/services/agenda.service';

@Component({
  selector: 'app-agenda',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './agenda.component.html'
})
export class AgendaComponent {

  form = this.fb.group({
    ordemMontagemId: [null, Validators.required],
    montadorId: [null, Validators.required],
    dataHora: ['', Validators.required]
  });

  confirmacao: string | null = null;

  constructor(private fb: FormBuilder, private agendaService: AgendaService) {}

  submeter(): void {
    if (this.form.invalid) {
      return;
    }

    this.agendaService.agendar(this.form.getRawValue() as any).subscribe(() => {
      this.confirmacao = 'Montagem agendada com sucesso.';
      this.form.reset();
    });
  }
}
