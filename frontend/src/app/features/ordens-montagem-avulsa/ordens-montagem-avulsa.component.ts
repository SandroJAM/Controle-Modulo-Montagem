import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormArray, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { OrdemMontagemService } from '../../core/services/ordem-montagem.service';

@Component({
  selector: 'app-ordens-montagem-avulsa',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './ordens-montagem-avulsa.component.html'
})
export class OrdensMontagemAvulsaComponent {

  private fb = inject(FormBuilder);
  private ordemMontagemService = inject(OrdemMontagemService);
  private router = inject(Router);

  form = this.fb.group({
    clienteNome: ['', Validators.required],
    clienteTelefone: [''],
    enderecoEntrega: ['', Validators.required],
    prazoCombinado: [''],
    valorServicoAvulso: [null as number | null],
    observacoes: [''],
    itens: this.fb.array([this.criarItemForm()])
  });

  get itens(): FormArray {
    return this.form.get('itens') as FormArray;
  }

  criarItemForm() {
    return this.fb.group({
      descricao: ['', Validators.required],
      quantidade: [1, [Validators.required, Validators.min(1)]]
    });
  }

  adicionarItem(): void {
    this.itens.push(this.criarItemForm());
  }

  removerItem(index: number): void {
    this.itens.removeAt(index);
  }

  submeter(): void {
    if (this.form.invalid) {
      return;
    }

    this.ordemMontagemService.criarAvulsa(this.form.getRawValue() as any).subscribe({
      next: () => this.router.navigate(['/ordens-montagem'])
    });
  }
}
