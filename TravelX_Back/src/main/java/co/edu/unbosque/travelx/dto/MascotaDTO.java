package co.edu.unbosque.travelx.dto;

import java.time.LocalDate;
import java.util.Objects;

public class MascotaDTO {
	
	private Long id;
	
	private String especie;
	
	private String raza;
	
	private LocalDate fechaNacimiento;
	
	public MascotaDTO() {
		// TODO Auto-generated constructor stub
	}

	public MascotaDTO(String especie, String raza, LocalDate fechaNacimiento) {
		super();
		this.especie = especie;
		this.raza = raza;
		this.fechaNacimiento = fechaNacimiento;
	}

	@Override
	public int hashCode() {
		return Objects.hash(especie, fechaNacimiento, id, raza);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MascotaDTO other = (MascotaDTO) obj;
		return Objects.equals(especie, other.especie) && Objects.equals(fechaNacimiento, other.fechaNacimiento)
				&& Objects.equals(id, other.id) && Objects.equals(raza, other.raza);
	}

	@Override
	public String toString() {
		return "MascotaDTO [id=" + id + ", especie=" + especie + ", raza=" + raza + ", fechaNacimiento="
				+ fechaNacimiento + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEspecie() {
		return especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	public String getRaza() {
		return raza;
	}

	public void setRaza(String raza) {
		this.raza = raza;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	
	
}
