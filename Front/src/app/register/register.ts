import { Component, HostBinding } from '@angular/core'; // 1. Importar HostBinding
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth';
import { MaterialModule } from '../material/material-module';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MaterialModule
  ],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class RegisterComponent {
  // --- MUDANÇA: Adicionar classe de fundo ao host ---
  @HostBinding('class') class = 'background-register'; // 2. Aplicar a classe

  credentials = {
    username: '',
    email: '',
    password: ''
  };
  errorMessage: string | null = null;

  constructor(private authService: AuthService, private router: Router) {}

  register(): void {
    this.authService.register(this.credentials).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.errorMessage = err.error.message || 'Erro ao tentar se registrar. Tente novamente mais tarde.';
      }
    });
  }
}
