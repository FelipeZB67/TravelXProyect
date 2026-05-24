import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { TravelOptionModel } from '../models/travel-option.model';

@Injectable({ providedIn: 'root' })

/**
 * Servicio para la gestión de reservas de viaje.
 * Permite agregar, consultar, crear, actualizar y eliminar reservas,
 * diferenciando entre operaciones de usuario y de administrador.
 */
export class ReservaService {
  private http = inject(HttpClient);
  private urlBase = `${environment.apiUrl}/reservas`;

  /**
   * Guarda una opción de viaje como reserva del usuario autenticado.
   *
   * @param option opción de viaje a reservar
   * @returns observable con la reserva creada
   */
  agregar(option: TravelOptionModel): Observable<TravelOptionModel> {
    return this.http.post<TravelOptionModel>(`${this.urlBase}/agregar`, option);
  }

  /** Retorna las reservas asociadas al usuario autenticado. */
  misReservas(): Observable<TravelOptionModel[]> {
    return this.http.get<TravelOptionModel[]>(`${this.urlBase}/mis-reservas`);
  }

  /** Retorna todas las reservas registradas en el sistema. Solo accesible para administradores. */
  todas(): Observable<TravelOptionModel[]> {
    return this.http.get<TravelOptionModel[]>(`${this.urlBase}/todas`);
  }

  /**
   * Crea una nueva reserva desde el panel de administración.
   *
   * @param reserva datos de la reserva a crear
   * @returns observable con la reserva creada
   */
  crearAdmin(reserva: TravelOptionModel): Observable<TravelOptionModel> {
    return this.http.post<TravelOptionModel>(`${this.urlBase}/admin/crear`, reserva);
  }

  /**
   * Actualiza una reserva existente desde el panel de administración.
   *
   * @param id identificador de la reserva a actualizar
   * @param reserva nuevos datos de la reserva
   * @returns observable con la reserva actualizada
   */
  actualizarAdmin(id: number, reserva: TravelOptionModel): Observable<TravelOptionModel> {
    return this.http.put<TravelOptionModel>(`${this.urlBase}/admin/actualizar/${id}`, reserva);
  }

  /**
   * Elimina una reserva del sistema desde el panel de administración.
   *
   * @param id identificador de la reserva a eliminar
   * @returns observable vacío al completarse la operación
   */
  eliminarAdmin(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlBase}/admin/eliminar/${id}`);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlBase}/${id}`);
  }

  /**
   * Obtiene el comprobante en texto de una reserva por su identificador.
   *
   * @param id identificador de la reserva
   * @returns observable con el comprobante en formato texto
   */
  imprimir(id: number): Observable<string> {
    return this.http.get(`${this.urlBase}/${id}/imprimir`, { responseType: 'text' });
  }
}
