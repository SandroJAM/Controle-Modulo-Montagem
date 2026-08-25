import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MontadorService } from '../../core/services/montador.service';
import { Montador } from '../../core/models/montador.model';

@Component({
  selector: 'app-montadores',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './montadores.component.html'
})
export class MontadoresComponent implements OnInit {

  montadores: Montador[] = [];

  form = this.fb.group({
    nome: ['', Validators.required],
    telefone: [''],
    areaAtuacao: [''],
    terceirizado: [false]
  });

  constructor(private fb: FormBuilder, private montadorService: MontadorService) {}

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.montadorService.listarAtivos().subscribe((montadores) => this.montadores = montadores);
  }

  submeter(): void {
    if (this.form.invalid) {
      return;
    }

    this.montadorService.criar({
      ...this.form.getRawValue(),
      ativo: true
    } as Montador).subscribe(() => {
      this.form.reset({ terceirizado: false });
      this.carregar();
    });
  }
}
