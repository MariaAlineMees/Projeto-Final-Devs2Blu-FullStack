import { Component, OnInit, HostBinding } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { RoteiroService, Roteiro } from '../services/roteiro';
import { MaterialModule } from '../material/material-module';

@Component({
  selector: 'app-roteiro-list',
  standalone: true,
  imports: [CommonModule, MaterialModule],
  templateUrl: './roteiro-list.html',
  styleUrls: ['./roteiro-list.css']
})
export class RoteiroListComponent implements OnInit {
  @HostBinding('class') class = 'background-roteiro-list';

  roteiros: Roteiro[] = [];

  constructor(private roteiroService: RoteiroService, private router: Router) {}

  ngOnInit(): void {
    this.carregarRoteiros();
  }

  carregarRoteiros(): void {
    this.roteiroService.listarRoteiros().subscribe(roteiros => {
      this.roteiros = roteiros;
    });
  }

  deletarRoteiro(id: number): void {
    if (confirm('Tem certeza que deseja deletar este roteiro?')) {
      this.roteiroService.deletarRoteiro(id).subscribe(() => {
        this.carregarRoteiros();
      });
    }
  }

  editarRoteiro(id: number): void {
    this.router.navigate(['/roteiros/editar', id]);
  }
}
