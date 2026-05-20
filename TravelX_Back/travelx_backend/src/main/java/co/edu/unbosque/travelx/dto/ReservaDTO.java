package co.edu.unbosque.travelx.dto;

import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "tipo"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ReservaNacionalDTO.class, name = "NACIONAL"),
    @JsonSubTypes.Type(value = ReservaInternacionalDTO.class, name = "INTERNACIONAL")
})
public abstract class ReservaDTO {
    
    private Long id;
    private Long personaId;
    private String hotel;
    private String metodoTransporte;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public ReservaDTO() {
    }

    public ReservaDTO(Long personaId, String hotel, String metodoTransporte, 
                     LocalDate fechaInicio, LocalDate fechaFin) {
        this.personaId = personaId;
        this.hotel = hotel;
        this.metodoTransporte = metodoTransporte;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
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

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getMetodoTransporte() {
        return metodoTransporte;
    }

    public void setMetodoTransporte(String metodoTransporte) {
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

	@Override
	public int hashCode() {
		return Objects.hash(fechaFin, fechaInicio, hotel, id, metodoTransporte, personaId);
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
		return Objects.equals(fechaFin, other.fechaFin) && Objects.equals(fechaInicio, other.fechaInicio)
				&& Objects.equals(hotel, other.hotel) && Objects.equals(id, other.id)
				&& Objects.equals(metodoTransporte, other.metodoTransporte)
				&& Objects.equals(personaId, other.personaId);
	}

	@Override
	public String toString() {
		return "ReservaDTO [id=" + id + ", personaId=" + personaId + ", hotel=" + hotel + ", metodoTransporte="
				+ metodoTransporte + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + "]";
	}
    
    
}