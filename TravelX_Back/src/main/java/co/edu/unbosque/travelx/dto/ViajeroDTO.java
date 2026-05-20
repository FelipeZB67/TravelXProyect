package co.edu.unbosque.travelx.dto;

import java.time.LocalDate;
import java.util.Objects;

public class ViajeroDTO {
	
	private Long id;
	
	private String nombre;
	
	private String pasaporte;
	
	private LocalDate fechaNacimiento;
	
	private boolean esMayor;
	
	public ViajeroDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public ViajeroDTO(String nombre, String pasaporte, LocalDate fechaNacimiento, boolean esMayor) {
		super();
		this.nombre = nombre;
		this.pasaporte = pasaporte;
		this.fechaNacimiento = fechaNacimiento;
		this.esMayor = esMayor;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(esMayor, fechaNacimiento, id, nombre, pasaporte);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ViajeroDTO other = (ViajeroDTO) obj;
		return esMayor == other.esMayor && Objects.equals(fechaNacimiento, other.fechaNacimiento)
				&& Objects.equals(id, other.id) && Objects.equals(nombre, other.nombre)
				&& Objects.equals(pasaporte, other.pasaporte);
	}


	@Override
	public String toString() {
		return "ViajeroDTO [id=" + id + ", nombre=" + nombre + ", pasaporte=" + pasaporte + ", fechaNacimiento="
				+ fechaNacimiento + ", esMayor=" + esMayor + "]";
	}


	public boolean isEsMayor() {
        return esMayor;
    }

    public void setEsMayor(boolean esMayor) {
        this.esMayor = esMayor;
    }


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getPasaporte() {
		return pasaporte;
	}


	public void setPasaporte(String pasaporte) {
		this.pasaporte = pasaporte;
	}


	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}


	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	
	
	

}
