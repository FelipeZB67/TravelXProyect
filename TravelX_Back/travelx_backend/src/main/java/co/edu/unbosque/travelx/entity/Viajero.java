package co.edu.unbosque.travelx.entity;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "viajero")
public class Viajero {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

	@ManyToOne
	@JoinColumn(name="reserva_id", nullable = false)
	private Reserva reserva;

	private String nombre;

	private String pasaporte;

	private LocalDate fechaNacimiento;

	private boolean esMayor;

	public Viajero() {
	}

	public Viajero(Reserva reserva, String nombre, String pasaporte, LocalDate fechaNacimiento) {
		this.reserva = reserva;
		this.nombre = nombre;
		this.pasaporte = pasaporte;
		this.fechaNacimiento = fechaNacimiento;
		calcularEdad();
	}

	@PrePersist
	private void calcularEdadAlInsertar() {
		calcularEdad();
	}

	@PreUpdate
	private void calcularEdadAlActualizar() {
		calcularEdad();
	}

	private void calcularEdad() {
		if (fechaNacimiento == null) {
			this.esMayor = false;
			return;
		}
		LocalDate hoy = LocalDate.now();
		int edad = Period.between(fechaNacimiento, hoy).getYears();
		this.esMayor = edad >= 18;
	}

	public boolean isEsMayor() {
		return esMayor;
	}

	@Override
	public String toString() {
		return "Viajero [id=" + id + ", reserva=" + reserva + ", nombre=" + nombre + ", pasaporte=" + pasaporte
				+ ", fechaNacimiento=" + fechaNacimiento + ", esMayor=" + esMayor + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(esMayor, fechaNacimiento, id, nombre, pasaporte, reserva);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Viajero other = (Viajero) obj;
		return esMayor == other.esMayor && Objects.equals(fechaNacimiento, other.fechaNacimiento)
				&& Objects.equals(id, other.id) && Objects.equals(nombre, other.nombre)
				&& Objects.equals(pasaporte, other.pasaporte) && Objects.equals(reserva, other.reserva);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPasaporte() {
		return pasaporte;
	}

	public void setPasaporte(String pasaporte) {
		this.pasaporte = pasaporte;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
		calcularEdad();
	}

	public void setEsMayor(boolean esMayor) {
		this.esMayor = esMayor;
	}
}