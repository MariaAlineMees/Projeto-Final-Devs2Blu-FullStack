import { Component } from '@angular/core';
import { RouterOutlet, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';

import { AuthService } from './services/auth';
import { MaterialModule } from './material/material-module';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterModule,
    CommonModule,
    HttpClientModule,
    MaterialModule
  ],
  providers: [AuthService],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  title = 'roteiro-front';
  currentYear: number;

  constructor(public authService: AuthService) {
    this.currentYear = new Date().getFullYear();
  }

  logout(): void {
    this.authService.logout();
  }
}
