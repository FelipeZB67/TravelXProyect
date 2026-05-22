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
@Table(name = "reserva_internacional")
public class ReservaInternacional extends Reserva{
	
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	
	private String paisOrigen; 
	
	private String paisDestino;
	
	private boolean requiereVisa;
	
	public ReservaInternacional() {
		super();	
	}

	public ReservaInternacional(Persona persona, List<MetodoTransporte> metodoTransporte, LocalDate fechaInicio,
			LocalDate fechaFin, String ciudadOrigen, String ciudadDestino, String hotel, List<Mascota> listaMascotas,
			List<Viajero> listaViajeros, String paisOrigen, String paisDestino, boolean requiereVisa) {
		super(persona, metodoTransporte, fechaInicio, fechaFin, ciudadOrigen, ciudadDestino, hotel, listaMascotas,
				listaViajeros);
		this.paisOrigen = paisOrigen;
		this.paisDestino = paisDestino;
		this.requiereVisa = requiereVisa;
	}

	public ReservaInternacional(Persona persona, List<MetodoTransporte> metodoTransporte, LocalDate fechaInicio,
			LocalDate fechaFin, String ciudadOrigen, String ciudadDestino, String hotel, List<Mascota> listaMascotas,
			List<Viajero> listaViajeros) {
		super(persona, metodoTransporte, fechaInicio, fechaFin, ciudadOrigen, ciudadDestino, hotel, listaMascotas,
				listaViajeros);
	}
	
	public ReservaInternacional(String paisOrigen, String paisDestino, boolean requiereVisa) {
		super();
		this.paisOrigen = paisOrigen;
		this.paisDestino = paisDestino;
		this.requiereVisa = requiereVisa;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPaisOrigen() {
		return paisOrigen;
	}

	public void setPaisOrigen(String paisOrigen) {
		this.paisOrigen = paisOrigen;
	}

	public String getPaisDestino() {
		return paisDestino;
	}

	public void setPaisDestino(String paisDestino) {
		this.paisDestino = paisDestino;
	}

	public boolean isRequiereVisa() {
		return requiereVisa;
	}

	public void setRequiereVisa(boolean requiereVisa) {
		this.requiereVisa = requiereVisa;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(id, paisDestino, paisOrigen, requiereVisa);
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
		ReservaInternacional other = (ReservaInternacional) obj;
		return Objects.equals(id, other.id) && Objects.equals(paisDestino, other.paisDestino)
				&& Objects.equals(paisOrigen, other.paisOrigen) && requiereVisa == other.requiereVisa;
	}

	@Override
	public String toString() {
		return super.toString() + 
				"ReservaInternacional [id=" + id + ", paisOrigen=" + paisOrigen + ", paisDestino=" + paisDestino
				+ ", requiereVisa=" + requiereVisa + "]";
	}
}