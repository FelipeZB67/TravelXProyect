package co.edu.unbosque.travelx.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reserva_nacional")
@DiscriminatorValue("NACIONAL")
public class ReservaNacional extends Reserva {

//	private String estadoOrigen;
	private String ciudadOrigen;

//	private String estadoDestino;
	private String ciudadDestino;

	public ReservaNacional() {
		super();
	}

	public ReservaNacional(String ciudadOrigen, String ciudadDestino) {
		super();
		this.ciudadOrigen = ciudadOrigen;
		this.ciudadDestino = ciudadDestino;
	}

	public ReservaNacional(Persona persona, String hotel, MetodoTransporte metodoTransporte, LocalDate fechaInicio,
			LocalDate fechaFin, List<Mascota> listaMascotas, List<Viajero> listaViajeros, String ciudadOrigen,
			String ciudadDestino) {
		super(persona, hotel, metodoTransporte, fechaInicio, fechaFin, listaMascotas, listaViajeros);
		this.ciudadOrigen = ciudadOrigen;
		this.ciudadDestino = ciudadDestino;
	}

	public ReservaNacional(Persona persona, String hotel, MetodoTransporte metodoTransporte, LocalDate fechaInicio,
			LocalDate fechaFin, List<Mascota> listaMascotas, List<Viajero> listaViajeros) {
		super(persona, hotel, metodoTransporte, fechaInicio, fechaFin, listaMascotas, listaViajeros);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ReservaNacional [ciudadOrigen=" + ciudadOrigen + ", ciudadDestino=" + ciudadDestino + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(ciudadDestino, ciudadOrigen);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReservaNacional other = (ReservaNacional) obj;
		return Objects.equals(ciudadDestino, other.ciudadDestino) && Objects.equals(ciudadOrigen, other.ciudadOrigen);
	}

	public String getCiudadOrigen() {
		return ciudadOrigen;
	}

	public void setCiudadOrigen(String ciudadOrigen) {
		this.ciudadOrigen = ciudadOrigen;
	}

	public String getCiudadDestino() {
		return ciudadDestino;
	}

	public void setCiudadDestino(String ciudadDestino) {
		this.ciudadDestino = ciudadDestino;
	}

}
