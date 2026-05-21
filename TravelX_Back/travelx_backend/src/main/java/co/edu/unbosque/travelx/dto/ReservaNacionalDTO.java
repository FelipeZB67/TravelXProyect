package co.edu.unbosque.travelx.dto;

import java.time.LocalDate;
import java.util.Objects;

public class ReservaNacionalDTO extends ReservaDTO{
	
	private String ciudadOrigen;
	private String ciudadDestino;
	
	public ReservaNacionalDTO() {
		// TODO Auto-generated constructor stub
	}

	public ReservaNacionalDTO(String ciudadOrigen, String ciudadDestino) {
		super();
		this.ciudadOrigen = ciudadOrigen;
		this.ciudadDestino = ciudadDestino;
	}

	public ReservaNacionalDTO(Long personaId, String hotel, String metodoTransporte, LocalDate fechaInicio,
			LocalDate fechaFin, String ciudadOrigen, String ciudadDestino) {
		super(personaId, hotel, metodoTransporte, fechaInicio, fechaFin);
		this.ciudadOrigen = ciudadOrigen;
		this.ciudadDestino = ciudadDestino;
	}

	public ReservaNacionalDTO(Long personaId, String hotel, String metodoTransporte, LocalDate fechaInicio,
			LocalDate fechaFin) {
		super(personaId, hotel, metodoTransporte, fechaInicio, fechaFin);
		// TODO Auto-generated constructor stub
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
		ReservaNacionalDTO other = (ReservaNacionalDTO) obj;
		return Objects.equals(ciudadDestino, other.ciudadDestino) && Objects.equals(ciudadOrigen, other.ciudadOrigen);
	}

	@Override
	public String toString() {
		return "ReservaNacionalDTO [ciudadOrigen=" + ciudadOrigen + ", ciudadDestino=" + ciudadDestino + "]";
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
