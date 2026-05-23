import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AirLabsService {

  private http    = inject(HttpClient);
  private UrlBase = `${environment.apiUrl}/airlabs`;

  obtenerTransbordos(indiceOrigen: number, indiceDestino: number): Observable<number> {
    return this.http.get<number>(`${this.UrlBase}/transbordos/${indiceOrigen}/${indiceDestino}`);
  }
}
