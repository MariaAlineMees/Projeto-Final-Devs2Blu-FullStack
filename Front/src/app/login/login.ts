import { Component, HostBinding } from '@angular/core'; // 1. Importar HostBinding
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../services/auth';
import { MaterialModule } from '../material/material-module';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MaterialModule
  ],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {

  @HostBinding('class') class = 'background-login';

  credentials = {
    username: '',
    password: ''
  };
  errorMessage: string | null = null;

  constructor(private authService: AuthService) {}

  login(): void {
    this.authService.login(this.credentials).subscribe({
      error: () => {
        this.errorMessage = 'Usuário ou senha inválidos.';
      }
    });
  }
}
