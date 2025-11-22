import { Routes } from '@angular/router';
import { LoginComponent } from './login/login';
import { RegisterComponent } from './register/register';
import { authGuard } from './services/auth';
import { HomeComponent } from './home/home';
import { RoteiroListComponent } from './roteiro/roteiro-list'; // Importa o RoteiroListComponent
import { RoteiroFormComponent } from './roteiro/roteiro-form'; // Importa o RoteiroFormComponent

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'home', component: HomeComponent, canActivate: [authGuard] },
    { path: 'roteiros', component: RoteiroListComponent, canActivate: [authGuard] }, // Rota para a lista
    { path: 'roteiros/novo', component: RoteiroFormComponent, canActivate: [authGuard] }, // Rota para criar
    { path: 'roteiros/editar/:id', component: RoteiroFormComponent, canActivate: [authGuard] }, // Rota para editar
    { path: '', redirectTo: '/login', pathMatch: 'full' }
];
