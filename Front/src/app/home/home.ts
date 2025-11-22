import { Component, HostBinding } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth';
import { Observable } from 'rxjs';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '../material/material-module'; // Importa o MaterialModule

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, MaterialModule], // Adiciona MaterialModule
  templateUrl: './home.html',
  styleUrls: ['./home.css'],
  host: {
    '[class]': "'background-roteiro'"
  }
})
export class HomeComponent {
  @HostBinding('class') class = 'background-roteiro';

  username$: Observable<string | null>;

  constructor(private authService: AuthService) {
    this.username$ = this.authService.currentUsername$;
  }
}
