import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RoteiroListComponent } from './roteiro-list';
import { RoteiroFormComponent } from './roteiro-form';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-roteiro',
  standalone: true,
  imports: [CommonModule, RoteiroListComponent, RoteiroFormComponent, RouterModule],
  template: `<router-outlet></router-outlet>`, // Apenas carrega as rotas filhas
  styles: []
})
export class RoteiroComponent {}
