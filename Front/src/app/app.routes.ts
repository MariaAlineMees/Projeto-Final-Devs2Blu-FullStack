import { Routes } from '@angular/router';
import { LoginComponent } from './login/login';
import { RegisterComponent } from './register/register';
import { authGuard } from './services/auth';
import { HomeComponent } from './home/home';
import { RoteiroListComponent } from './roteiro/roteiro-list';
import { RoteiroFormComponent } from './roteiro/roteiro-form';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'home', component: HomeComponent, canActivate: [authGuard] },
    { path: 'roteiros', component: RoteiroListComponent, canActivate: [authGuard] },
    { path: 'roteiros/novo', component: RoteiroFormComponent, canActivate: [authGuard] },
    { path: 'roteiros/editar/:id', component: RoteiroFormComponent, canActivate: [authGuard] },
    { path: '', redirectTo: '/login', pathMatch: 'full' }
];
