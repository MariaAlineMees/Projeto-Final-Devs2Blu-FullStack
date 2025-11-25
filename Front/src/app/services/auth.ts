import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { CanActivateFn, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = '/api';

  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  private currentUsernameSubject = new BehaviorSubject<string | null>(null);
  public currentUsername$ = this.currentUsernameSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    this.verifyUserStatus();
  }

  private verifyUserStatus() {
    this.fetchCurrentUser().subscribe();
  }

  fetchCurrentUser(): Observable<string | null> {
    return this.http.get(`${this.apiUrl}/auth/me`, { withCredentials: true, responseType: 'text' }).pipe(
      tap(username => {
        this.isAuthenticatedSubject.next(true);
        this.currentUsernameSubject.next(username);
      }),
      catchError((error) => {

        if (error.status === 401) {
          this.isAuthenticatedSubject.next(false);
          this.currentUsernameSubject.next(null);
        }

        console.error('Erro ao buscar usuário atual:', error);
        return of(null);
      })
    );
  }

  register(credentials: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/auth/register`, credentials, { withCredentials: true });
  }

  login(credentials: any): Observable<boolean> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded'
    });
    const body = `username=${encodeURIComponent(credentials.username)}&password=${encodeURIComponent(credentials.password)}`;

    return this.http.post(`${this.apiUrl}/login`, body, { headers, withCredentials: true, responseType: 'text' }).pipe(
      map(() => {

        this.fetchCurrentUser().subscribe({
          next: () => this.router.navigate(['/home']),
          error: () => this.router.navigate(['/login'])
        });
        return true;
      }),
      catchError((error) => {
        console.error('Erro no login:', error);
        this.isAuthenticatedSubject.next(false);
        this.currentUsernameSubject.next(null);
        return of(false);
      })
    );
  }

  logout(): void {
    this.http.post(`${this.apiUrl}/logout`, {}, { withCredentials: true }).pipe(
      tap(() => {
        this.isAuthenticatedSubject.next(false);
        this.currentUsernameSubject.next(null);
        this.router.navigate(['/login']);
      }),
      catchError((error) => {
        console.error('Erro no logout:', error);

        this.isAuthenticatedSubject.next(false);
        this.currentUsernameSubject.next(null);
        this.router.navigate(['/login']);
        return of(null);
      })
    ).subscribe();
  }
}

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.fetchCurrentUser().pipe(
    map(user => {
      if (user) {
        return true;
      } else {
        return router.parseUrl('/login');
      }
    }),
    catchError(() => {
      return of(router.parseUrl('/login'));
    })
  );
};
