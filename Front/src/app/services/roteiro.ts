import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Roteiro {
  id?: number;
  titulo: string;
  destino: string;
  dataInicio: string;
  dataFim: string;
  custoEstimado: number;
}

@Injectable({
  providedIn: 'root'
})
export class RoteiroService {
  private apiUrl = '/api/roteiros';

  constructor(private http: HttpClient) { }

  // CORRIGIDO: Renomeado de getRoteiros para listarRoteiros para consistência
  listarRoteiros(): Observable<Roteiro[]> {
    return this.http.get<Roteiro[]>(this.apiUrl, { withCredentials: true });
  }

  // ADICIONADO: Método que faltava para buscar um roteiro por ID
  buscarRoteiroPorId(id: number): Observable<Roteiro> {
    return this.http.get<Roteiro>(`${this.apiUrl}/${id}`, { withCredentials: true });
  }

  criarRoteiro(roteiro: Roteiro): Observable<Roteiro> {
    return this.http.post<Roteiro>(this.apiUrl, roteiro, { withCredentials: true });
  }

  // CORRIGIDO: Agora aceita id e roteiro, como o novo RoteiroFormComponent espera
  atualizarRoteiro(id: number, roteiro: Roteiro): Observable<Roteiro> {
    return this.http.put<Roteiro>(`${this.apiUrl}/${id}`, roteiro, { withCredentials: true });
  }

  deletarRoteiro(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { withCredentials: true });
  }
}
