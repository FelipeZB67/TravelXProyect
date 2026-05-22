package co.edu.unbosque.travelx.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import co.edu.unbosque.travelx.entity.Reserva.MetodoTransporte;


import co.edu.unbosque.travelx.entity.Reserva.MetodoTransporte;

/*@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "tipo"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ReservaNacionalDTO.class, name = "NACIONAL"),
    @JsonSubTypes.Type(value = ReservaInternacionalDTO.class, name = "INTERNACIONAL")
})*/

public abstract class ReservaDTO {
    
    private Long id;
    private Long personaId;
    

    private MetodoTransporte metodoTransporte;
    
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    
    private String ciudadOrigen;
	private String ciudadDestino;
	
	private double precioTransporte;
    private String hotel;
    private double precioHospedaje;
    


	public Long getId() {
		return id;
	}



	public ReservaDTO(Long personaId, MetodoTransporte metodoTransporte2, LocalDate fechaInicio, LocalDate fechaFin,
			String ciudadOrigen, String ciudadDestino, double precioTransporte, String hotel, double precioHospedaje) {
		super();
		this.personaId = personaId;
		this.metodoTransporte = metodoTransporte2;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.ciudadOrigen = ciudadOrigen;
		this.ciudadDestino = ciudadDestino;
		this.precioTransporte = precioTransporte;
		this.hotel = hotel;
		this.precioHospedaje = precioHospedaje;
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


	public MetodoTransporte getMetodoTransporte() {
		return metodoTransporte;
	}


	public void setMetodoTransporte(MetodoTransporte metodoTransporte) {
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


	public double getPrecioTransporte() {
		return precioTransporte;
	}


	public void setPrecioTransporte(double precioTransporte) {
		this.precioTransporte = precioTransporte;
	}


	public String getHotel() {
		return hotel;
	}


	public void setHotel(String hotel) {
		this.hotel = hotel;
	}


	public double getPrecioHospedaje() {
		return precioHospedaje;
	}


	public void setPrecioHospedaje(double precioHospedaje) {
		this.precioHospedaje = precioHospedaje;
	}


	@Override
	public int hashCode() {
		return Objects.hash(ciudadDestino, ciudadOrigen, fechaFin, fechaInicio, hotel, id, metodoTransporte, personaId,
				precioHospedaje, precioTransporte);

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
				&& Objects.equals(personaId, other.personaId)
				&& Double.doubleToLongBits(precioHospedaje) == Double.doubleToLongBits(other.precioHospedaje)
				&& Double.doubleToLongBits(precioTransporte) == Double.doubleToLongBits(other.precioTransporte);
	}


	@Override
	public String toString() {
		return "ReservaDTO [id=" + id + ", personaId=" + personaId + ", metodoTransporte=" + metodoTransporte
				+ ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", ciudadOrigen=" + ciudadOrigen

				+ ", ciudadDestino=" + ciudadDestino + ", precioTransporte=" + precioTransporte + ", hotel=" + hotel
				+ ", precioHospedaje=" + precioHospedaje + "]";
	}    
    
}