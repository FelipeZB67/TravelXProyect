package co.edu.unbosque.travelx.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reserva_nacional")
public class ReservaNacional extends Reserva {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

	public ReservaNacional() {
		super();
	}

	public ReservaNacional(Persona persona, List<MetodoTransporte> metodoTransporte, LocalDate fechaInicio,
			LocalDate fechaFin, String ciudadOrigen, String ciudadDestino, String hotel, List<Mascota> listaMascotas,
			List<Viajero> listaViajeros) {
		super(persona, metodoTransporte, fechaInicio, fechaFin, ciudadOrigen, ciudadDestino, hotel, listaMascotas,
				listaViajeros);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(id);
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
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return super.toString() + 
				"ReservaNacional [id=" + id + "]";
	}
}