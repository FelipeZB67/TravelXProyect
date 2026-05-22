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
	private List<MetodoTransporte> metodoTransporte;
	
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	
	private String ciudadOrigen;
	private String ciudadDestino;
	private double precioTransporte;
	private String hotel;
	private double precioHospedaje;
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


	

	public Reserva(Persona persona, MetodoTransporte metodoTransporte, LocalDate fechaInicio, LocalDate fechaFin,
			String ciudadOrigen, String ciudadDestino, double precioTransporte, String hotel, double precioHospedaje,
			List<Mascota> listaMascotas, List<Viajero> listaViajeros) {

		super();
		this.persona = persona;
		this.metodoTransporte = metodoTransporte;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.ciudadOrigen = ciudadOrigen;
		this.ciudadDestino = ciudadDestino;

		this.precioTransporte = precioTransporte;
		this.hotel = hotel;
		this.precioHospedaje = precioHospedaje;

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
				&& Objects.equals(listaViajeros, other.listaViajeros)
				&& Objects.equals(metodoTransporte, other.metodoTransporte) && Objects.equals(persona, other.persona);
	}

	@Override
	public String toString() {
		return "Reserva [id=" + id + ", persona=" + persona + ", metodoTransporte=" + metodoTransporte
				+ ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", ciudadOrigen=" + ciudadOrigen
				+ ", ciudadDestino=" + ciudadDestino + ", hotel=" + hotel + ", listaMascotas=" + listaMascotas
				+ ", listaViajeros=" + listaViajeros + "]";
	}

	public double getPrecioTransporte() {
		return precioTransporte;
	}

	public void setPrecioTransporte(double precioTransporte) {
		this.precioTransporte = precioTransporte;
	}

	public double getPrecioHospedaje() {
		return precioHospedaje;
	}

	public void setPrecioHospedaje(double precioHospedaje) {
		this.precioHospedaje = precioHospedaje;
	}
}