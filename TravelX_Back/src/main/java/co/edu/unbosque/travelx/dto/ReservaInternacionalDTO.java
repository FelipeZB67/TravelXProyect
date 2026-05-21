package co.edu.unbosque.travelx.dto;

import java.time.LocalDate;
import java.util.Objects;

import co.edu.unbosque.travelx.entity.Reserva.MetodoTransporte;

public class ReservaInternacionalDTO extends ReservaDTO{
	
	private String paisOrigen;
	private String ciudadOrigen;
	
	private String paisDestino;
	private String ciudadDestino;
	
	private boolean requiereVisa;
	
	public ReservaInternacionalDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public ReservaInternacionalDTO(Long personaId, MetodoTransporte metodoTransporte, LocalDate fechaInicio, LocalDate fechaFin,
			String ciudadOrigen, String ciudadDestino, double precioTransporte, String hotel, double precioHospedaje) {
		super(personaId, metodoTransporte, fechaInicio, fechaFin, ciudadOrigen, ciudadDestino, precioTransporte, hotel,
				precioHospedaje);
		// TODO Auto-generated constructor stub
	}

	public ReservaInternacionalDTO(Long personaId, MetodoTransporte metodoTransporte, LocalDate fechaInicio, LocalDate fechaFin,
			String ciudadOrigen, String ciudadDestino, double precioTransporte, String hotel, double precioHospedaje,
			String paisOrigen, String ciudadOrigen2, String paisDestino, String ciudadDestino2, boolean requiereVisa) {
		super(personaId, metodoTransporte, fechaInicio, fechaFin, ciudadOrigen, ciudadDestino, precioTransporte, hotel,
				precioHospedaje);
		this.paisOrigen = paisOrigen;
		ciudadOrigen = ciudadOrigen2;
		this.paisDestino = paisDestino;
		ciudadDestino = ciudadDestino2;
		this.requiereVisa = requiereVisa;
	}





	public ReservaInternacionalDTO(String paisOrigen, String ciudadOrigen, String paisDestino, String ciudadDestino,
			boolean requiereVisa) {
		super();
		this.paisOrigen = paisOrigen;
		this.ciudadOrigen = ciudadOrigen;
		this.paisDestino = paisDestino;
		this.ciudadDestino = ciudadDestino;
		this.requiereVisa = requiereVisa;
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
		ReservaInternacionalDTO other = (ReservaInternacionalDTO) obj;
		return Objects.equals(ciudadDestino, other.ciudadDestino) && Objects.equals(ciudadOrigen, other.ciudadOrigen)
				&& Objects.equals(paisDestino, other.paisDestino) && Objects.equals(paisOrigen, other.paisOrigen)
				&& requiereVisa == other.requiereVisa;
	}

	@Override
	public String toString() {
		return "ReservaInternacionalDTO [paisOrigen=" + paisOrigen + ", ciudadOrigen=" + ciudadOrigen + ", paisDestino="
				+ paisDestino + ", ciudadDestino=" + ciudadDestino + ", requiereVisa=" + requiereVisa + "]";
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
