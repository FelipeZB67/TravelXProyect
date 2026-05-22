package co.edu.unbosque.travelx.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import co.edu.unbosque.travelx.entity.Reserva.MetodoTransporte;

public class ReservaNacionalDTO extends ReservaDTO{
	
	private Long id;
	
	public ReservaNacionalDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public ReservaNacionalDTO(Long personaId, List<MetodoTransporte> metodoTransporte, LocalDate fechaInicio,
			LocalDate fechaFin, String ciudadOrigen, String ciudadDestino, String hotel, List<MascotaDTO> listaMascotas,
			List<ViajeroDTO> listaViajeros) {
		super(personaId, metodoTransporte, fechaInicio, fechaFin, ciudadOrigen, ciudadDestino, hotel, listaMascotas,
				listaViajeros);
		// TODO Auto-generated constructor stub
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
		ReservaNacionalDTO other = (ReservaNacionalDTO) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "ReservaNacionalDTO [id=" + id + "]";
	}
}
