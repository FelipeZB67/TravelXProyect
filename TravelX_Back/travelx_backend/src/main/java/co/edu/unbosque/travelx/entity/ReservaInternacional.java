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
@Table(name = "reserva_internacional")
@DiscriminatorValue("INTERNACIONAL")
public class ReservaInternacional extends Reserva{
	
	private String paisOrigen; 
	private String ciudadOrigen;
	
	private String paisDestino;
	private String ciudadDestino;
	
	private boolean requiereVisa;
	
	public ReservaInternacional() {
		super();	
	}

	public ReservaInternacional(String paisOrigen, String ciudadOrigen, String paisDestino, String ciudadDestino,
			boolean requiereVisa) {
		super();
		this.paisOrigen = paisOrigen;
		this.ciudadOrigen = ciudadOrigen;
		this.paisDestino = paisDestino;
		this.ciudadDestino = ciudadDestino;
		this.requiereVisa = requiereVisa;
	}

	public ReservaInternacional(Persona persona, String hotel, MetodoTransporte metodoTransporte, LocalDate fechaInicio,
			LocalDate fechaFin, List<Mascota> listaMascotas, List<Viajero> listaViajeros, String paisOrigen,
			String ciudadOrigen, String paisDestino, String ciudadDestino, boolean requiereVisa) {
		super(persona, hotel, metodoTransporte, fechaInicio, fechaFin, listaMascotas, listaViajeros);
		this.paisOrigen = paisOrigen;
		this.ciudadOrigen = ciudadOrigen;
		this.paisDestino = paisDestino;
		this.ciudadDestino = ciudadDestino;
		this.requiereVisa = requiereVisa;
	}

	public ReservaInternacional(Persona persona, String hotel, MetodoTransporte metodoTransporte, LocalDate fechaInicio,
			LocalDate fechaFin, List<Mascota> listaMascotas, List<Viajero> listaViajeros) {
		super(persona, hotel, metodoTransporte, fechaInicio, fechaFin, listaMascotas, listaViajeros);
		// TODO Auto-generated constructor stub
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(ciudadDestino, ciudadOrigen, paisDestino, paisOrigen, requiereVisa);
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
		return Objects.equals(ciudadDestino, other.ciudadDestino) && Objects.equals(ciudadOrigen, other.ciudadOrigen)
				&& Objects.equals(paisDestino, other.paisDestino) && Objects.equals(paisOrigen, other.paisOrigen)
				&& requiereVisa == other.requiereVisa;
	}

	

	@Override
	public String toString() {
		return "ReservaInternacional [paisOrigen=" + paisOrigen + ", ciudadOrigen=" + ciudadOrigen + ", paisDestino="
				+ paisDestino + ", ciudadDestino=" + ciudadDestino + ", rquiereVisa=" + requiereVisa + "]";
	}

	public String getPaisOrigen() {
		return paisOrigen;
	}

	public void setPaisOrigen(String paisOrigen) {
		this.paisOrigen = paisOrigen;
	}

	public String getCiudadOrigen() {
		return ciudadOrigen;
	}

	public void setCiudadOrigen(String ciudadOrigen) {
		this.ciudadOrigen = ciudadOrigen;
	}

	public String getPaisDestino() {
		return paisDestino;
	}

	public void setPaisDestino(String paisDestino) {
		this.paisDestino = paisDestino;
	}

	public String getCiudadDestino() {
		return ciudadDestino;
	}

	public void setCiudadDestino(String ciudadDestino) {
		this.ciudadDestino = ciudadDestino;
	}

	public boolean isRequiereVisa() {
		return requiereVisa;
	}

	public void setRequiereVisa(boolean requiereVisa) {
		this.requiereVisa = requiereVisa;
	}
	
	
}
