import { Component, OnInit, HostBinding } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { RoteiroService, Roteiro } from '../services/roteiro';
import { MaterialModule } from '../material/material-module';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-roteiro-form',
  standalone: true,
  imports: [CommonModule, FormsModule, MaterialModule],
  templateUrl: './roteiro-form.html',
  styleUrls: ['./roteiro-form.css']
})
export class RoteiroFormComponent implements OnInit {
  @HostBinding('class') class = 'background-roteiro-form';

  novoRoteiro: Roteiro = {
    id: 0,
    titulo: '',
    destino: '',
    dataInicio: '',
    dataFim: '',
    custoEstimado: 0
  };
  isEditing = false;

  constructor(
    private roteiroService: RoteiroService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    console.log('ID da rota:', id);

    if (id) {
      this.isEditing = true;
      console.log('Modo de edição ativado.'); // DEBUG

      this.roteiroService.buscarRoteiroPorId(+id).subscribe({
        next: (roteiro) => {
          console.log('Dados do roteiro recebidos:', roteiro);
          this.novoRoteiro = roteiro;
        },
        error: (err) => {
          console.error('Erro ao buscar roteiro por ID:', err);
        }
      });
    }
  }

  salvarRoteiro(): void {
    if (this.isEditing && this.novoRoteiro.id) {
      this.roteiroService.atualizarRoteiro(this.novoRoteiro.id, this.novoRoteiro).subscribe(() => {
        this.snackBar.open('Roteiro atualizado com sucesso!', 'Fechar', { duration: 3000 });
        this.router.navigate(['/roteiros']);
      });
    } else {
      this.roteiroService.criarRoteiro(this.novoRoteiro).subscribe(() => {
        this.snackBar.open('Roteiro criado com sucesso!', 'Fechar', { duration: 3000 });
        this.router.navigate(['/roteiros']);
      });
    }
  }

  resetForm(): void {
    this.isEditing = false;
    this.novoRoteiro = {
      id: 0,
      titulo: '',
      destino: '',
      dataInicio: '',
      dataFim: '',
      custoEstimado: 0
    };
    this.router.navigate(['/roteiros/novo']);
  }
}
