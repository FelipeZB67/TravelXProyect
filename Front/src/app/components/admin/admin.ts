import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin',
  standalone: false,
  templateUrl: './admin.html',
  styleUrls: ['./admin.css']
})
export class AdminComponent {
  seccionActiva: 'usuarios' | 'administradores' | 'reservas' = 'usuarios';
  modalAbierto = false;
  modoEdicion = false;
  itemEditando: any = null;

  constructor(private router: Router) {}

  usuarios: any[] = [
    { id: 1, nombre: 'Carlos Pérez', email: 'carlos@email.com', estado: 'Activo' },
    { id: 2, nombre: 'María López', email: 'maria@email.com', estado: 'Inactivo' },
    { id: 3, nombre: 'Juan García', email: 'juan@email.com', estado: 'Activo' },
  ];

  administradores: any[] = [
    { id: 1, nombre: 'Admin Principal', email: 'admin@travelx.com', rol: 'Superadmin' },
    { id: 2, nombre: 'Laura Martínez', email: 'laura@travelx.com', rol: 'Moderador' },
  ];

  reservas: any[] = [
    { id: 1, usuario: 'Carlos Pérez', destino: 'París', fecha: '2025-06-15', estado: 'Confirmada', precio: 1200 },
    { id: 2, usuario: 'María López', destino: 'Cartagena', fecha: '2025-07-01', estado: 'Pendiente', precio: 95 },
    { id: 3, usuario: 'Juan García', destino: 'Tokio', fecha: '2025-08-20', estado: 'Cancelada', precio: 1800 },
  ];

  formulario: any = {};

  get datosActivos(): any[] {
    if (this.seccionActiva === 'usuarios') return this.usuarios;
    if (this.seccionActiva === 'administradores') return this.administradores;
    return this.reservas;
  }

  get columnasActivas(): string[] {
    if (this.seccionActiva === 'usuarios') return ['id', 'nombre', 'email', 'estado'];
    if (this.seccionActiva === 'administradores') return ['id', 'nombre', 'email', 'rol'];
    return ['id', 'usuario', 'destino', 'fecha', 'estado', 'precio'];
  }

  abrirModalCrear() {
    this.formulario = {};
    this.modoEdicion = false;
    this.modalAbierto = true;
  }

  abrirModalEditar(item: any) {
    this.formulario = { ...item };
    this.itemEditando = item;
    this.modoEdicion = true;
    this.modalAbierto = true;
  }

  guardar() {
    if (this.modoEdicion) {
      Object.assign(this.itemEditando, this.formulario);
    } else {
      const lista = this.datosActivos;
      this.formulario.id = lista.length + 1;
      lista.push({ ...this.formulario });
    }
    this.modalAbierto = false;
  }

  eliminar(item: any) {
    if (this.seccionActiva === 'usuarios') this.usuarios = this.usuarios.filter(u => u !== item);
    if (this.seccionActiva === 'administradores') this.administradores = this.administradores.filter(a => a !== item);
    if (this.seccionActiva === 'reservas') this.reservas = this.reservas.filter(r => r !== item);
  }

  cerrarSesion() {
    this.router.navigate(['/login']);
  }
}
