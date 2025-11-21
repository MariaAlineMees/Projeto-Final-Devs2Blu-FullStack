import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class RegisterComponent {
  credentials = {
    username: '',
    email: '', // Adicionado o campo de e-mail
    password: ''
  };
  errorMessage: string | null = null;

  constructor(private authService: AuthService, private router: Router) {}

  register(): void {
    this.authService.register(this.credentials).subscribe({
      next: () => {
        // Redireciona para a página de login após o registro bem-sucedido
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.errorMessage = err.error.message || 'Erro ao tentar se registrar. Tente novamente mais tarde.';
      }
    });
  }
}
