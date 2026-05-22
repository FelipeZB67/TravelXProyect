package co.edu.unbosque.travelx.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import co.edu.unbosque.travelx.entity.Reserva.MetodoTransporte;

public abstract class ReservaDTO {
    
    private Long id;
    private Long personaId;
    
private List<MetodoTransporte> metodoTransporte;
	
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	
	private String ciudadOrigen;
	private String ciudadDestino;
	private String hotel;
	private List<MascotaDTO> listaMascotas;
	private List<ViajeroDTO> listaViajeros;
	
	public ReservaDTO() {
		
	}
	
	public ReservaDTO(Long personaId, List<MetodoTransporte> metodoTransporte, LocalDate fechaInicio,
			LocalDate fechaFin, String ciudadOrigen, String ciudadDestino, String hotel, List<MascotaDTO> listaMascotas,
			List<ViajeroDTO> listaViajeros) {
		super();
		this.personaId = personaId;
		this.metodoTransporte = metodoTransporte;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.ciudadOrigen = ciudadOrigen;
		this.ciudadDestino = ciudadDestino;
		this.hotel = hotel;
		this.listaMascotas = listaMascotas;
		this.listaViajeros = listaViajeros;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPersonaId() {
		return personaId;
	}

	public void setPersonaId(Long personaId) {
		this.personaId = personaId;
	}

	public List<MetodoTransporte> getMetodoTransporte() {
		return metodoTransporte;
	}

	public void setMetodoTransporte(List<MetodoTransporte> metodoTransporte) {
		this.metodoTransporte = metodoTransporte;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
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

	public String getHotel() {
		return hotel;
	}

	public void setHotel(String hotel) {
		this.hotel = hotel;
	}

	public List<MascotaDTO> getListaMascotas() {
		return listaMascotas;
	}

	public void setListaMascotas(List<MascotaDTO> listaMascotas) {
		this.listaMascotas = listaMascotas;
	}

	public List<ViajeroDTO> getListaViajeros() {
		return listaViajeros;
	}

	public void setListaViajeros(List<ViajeroDTO> listaViajeros) {
		this.listaViajeros = listaViajeros;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ciudadDestino, ciudadOrigen, fechaFin, fechaInicio, hotel, id, listaMascotas, listaViajeros,
				metodoTransporte, personaId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReservaDTO other = (ReservaDTO) obj;
		return Objects.equals(ciudadDestino, other.ciudadDestino) && Objects.equals(ciudadOrigen, other.ciudadOrigen)
				&& Objects.equals(fechaFin, other.fechaFin) && Objects.equals(fechaInicio, other.fechaInicio)
				&& Objects.equals(hotel, other.hotel) && Objects.equals(id, other.id)
				&& Objects.equals(listaMascotas, other.listaMascotas)
				&& Objects.equals(listaViajeros, other.listaViajeros)
				&& Objects.equals(metodoTransporte, other.metodoTransporte)
				&& Objects.equals(personaId, other.personaId);
	}

	@Override
	public String toString() {
		return "ReservaDTO [id=" + id + ", personaId=" + personaId + ", metodoTransporte=" + metodoTransporte
				+ ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", ciudadOrigen=" + ciudadOrigen
				+ ", ciudadDestino=" + ciudadDestino + ", hotel=" + hotel + ", listaMascotas=" + listaMascotas
				+ ", listaViajeros=" + listaViajeros + "]";
	}
}