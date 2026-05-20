package co.edu.unbosque.travelx.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "reserva")
@Inheritance(strategy = InheritanceType.JOINED)
public class Reserva {
	
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	
	@ManyToOne
	@JoinColumn(name = "persona_id", nullable = false)
	private Persona persona;
	
	
	@Enumerated(EnumType.STRING)
	private MetodoTransporte metodoTransporte;
	
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	
	private String ciudadOrigen;
	private String ciudadDestino;
	private String hotel;
	
	@JsonIgnore
	@OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL)
	private List<Mascota> listaMascotas;

	@JsonIgnore
	@OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL)
	private List<Viajero> listaViajeros;
	
	public enum MetodoTransporte {
		TERRESTRE, NAVAL, AEREO
	}
	
	public Reserva() {
		// TODO Auto-generated constructor stub
	}

	public Reserva(Persona persona, String hotel, MetodoTransporte metodoTransporte, LocalDate fechaInicio,
			LocalDate fechaFin, String ciudadOrigen, String ciudadDestino, List<Mascota> listaMascotas,
			List<Viajero> listaViajeros) {
		this.persona = persona;
		this.hotel = hotel;
		this.metodoTransporte = metodoTransporte;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.ciudadOrigen = ciudadOrigen;
		this.ciudadDestino = ciudadDestino;
		this.listaMascotas = listaMascotas;
		this.listaViajeros = listaViajeros;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public String getHotel() {
		return hotel;
	}

	public void setHotel(String hotel) {
		this.hotel = hotel;
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

	public List<Mascota> getListaMascotas() {
		return listaMascotas;
	}

	public void setListaMascotas(List<Mascota> listaMascotas) {
		this.listaMascotas = listaMascotas;
	}

	public List<Viajero> getListaViajeros() {
		return listaViajeros;
	}

	public void setListaViajeros(List<Viajero> listaViajeros) {
		this.listaViajeros = listaViajeros;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ciudadDestino, ciudadOrigen, fechaFin, fechaInicio, hotel, id, listaMascotas, listaViajeros,
				metodoTransporte, persona);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reserva other = (Reserva) obj;
		return Objects.equals(ciudadDestino, other.ciudadDestino) && Objects.equals(ciudadOrigen, other.ciudadOrigen)
				&& Objects.equals(fechaFin, other.fechaFin) && Objects.equals(fechaInicio, other.fechaInicio)
				&& Objects.equals(hotel, other.hotel) && Objects.equals(id, other.id)
				&& Objects.equals(listaMascotas, other.listaMascotas)
				&& Objects.equals(listaViajeros, other.listaViajeros) && metodoTransporte == other.metodoTransporte
				&& Objects.equals(persona, other.persona);
	}

	@Override
	public String toString() {
		return "Reserva [id=" + id + ", persona=" + persona + ", hotel=" + hotel + ", metodoTransporte="
				+ metodoTransporte + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", ciudadOrigen="
				+ ciudadOrigen + ", ciudadDestino=" + ciudadDestino + ", listaMascotas=" + listaMascotas
				+ ", listaViajeros=" + listaViajeros + "]";
	}
}