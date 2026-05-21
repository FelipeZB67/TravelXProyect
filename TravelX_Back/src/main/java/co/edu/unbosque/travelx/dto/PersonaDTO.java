package co.edu.unbosque.travelx.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import co.edu.unbosque.travelx.entity.Persona.TipoUsuario;

public class PersonaDTO {
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;
	
	private String nombre;
	
	private String documento;
	
	private String correo;
	
	private String contrasena;
	
	private TipoUsuario tipoUsuario;
	
	public PersonaDTO() {
		// TODO Auto-generated constructor stub
	}

	public PersonaDTO(String nombre, String documento, String correo, String contrasena, TipoUsuario tipoUsuario) {
		super();
		this.nombre = nombre;
		this.documento = documento;
		this.correo = correo;
		this.contrasena = contrasena;
		this.tipoUsuario = tipoUsuario;
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

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}

	public TipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	@Override
	public String toString() {
		return "PersonaDTO [id=" + id + ", nombre=" + nombre + ", documento=" + documento + ", correo=" + correo
				+ ", contrasena=" + contrasena + ", tipoUsuario=" + tipoUsuario + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(contrasena, correo, documento, id, nombre, tipoUsuario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonaDTO other = (PersonaDTO) obj;
		return Objects.equals(contrasena, other.contrasena) && Objects.equals(correo, other.correo)
				&& Objects.equals(documento, other.documento) && Objects.equals(id, other.id)
				&& Objects.equals(nombre, other.nombre) && tipoUsuario == other.tipoUsuario;
	}
	
	

}
