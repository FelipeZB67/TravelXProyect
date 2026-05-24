package co.edu.unbosque.travelx.entity;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad que representa una persona registrada en el sistema.
 * Implementa {@link UserDetails} para integrarse con Spring Security,
 * gestionando autenticación, roles y verificación de correo electrónico.
 */
@Entity
@Table(name = "persona")
public class Persona implements UserDetails {

	private static final long serialVersionUID = 57710312743121686L;

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

	@Column(unique = true)
	private String nombre;

	@Column(unique = true)
	private String documento;

	@Column(unique = true)
	private String correo;

	private String contrasena;

	@Enumerated(EnumType.STRING)
	private TipoUsuario tipoUsuario;

	private String codigoVerificacion;

	private boolean correoVerificado;

	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	private boolean enabled;

	/**
	 * Constructor por defecto que inicializa una persona con la cuenta activa,
	 * no bloqueada, credenciales vigentes, correo sin verificar y rol {@code NINGUNO}.
	 */
	public Persona() {
		this.accountNonExpired = true;
		this.accountNonLocked = true;
		this.credentialsNonExpired = true;
		this.enabled = false;
		this.correoVerificado = false;
		this.tipoUsuario = TipoUsuario.NINGUNO;
	}
	
	public Persona(String nombre, String documento, String correo, String contrasena) {
		super();
		this.nombre = nombre;
		this.documento = documento;
		this.correo = correo;
		this.contrasena = contrasena;

	}

	public Persona(String nombre, String documento, String correo, String contrasena, TipoUsuario tipoUsuario) {
		super();
		this.nombre = nombre;
		this.documento = documento;
		this.correo = correo;
		this.contrasena = contrasena;
		this.tipoUsuario = tipoUsuario;
	}

	/**
	 * Define los roles disponibles para una persona en el sistema.
	 * {@code ADMINISTRADOR} tiene acceso total, {@code USUARIO} acceso estándar
	 * y {@code NINGUNO} indica que aún no se ha asignado un rol.
	 */
	public enum TipoUsuario {
		ADMINISTRADOR, USUARIO, NINGUNO
	}

	/**
	 * Retorna la lista de roles asignados a la persona en formato requerido
	 * por Spring Security, construyendo el rol como {@code ROLE_<tipoUsuario>}.
	 *
	 * @return colección con la autoridad asignada según el tipo de usuario
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + tipoUsuario.name()));
	}

	@Override
	public String getPassword() {
		return contrasena;
	}

	@Override
	public String getUsername() {
		return correo;
	}

	@Override
	public String toString() {
		return "Persona [id=" + id + ", nombre=" + nombre + ", documento=" + documento + ", correo=" + correo
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
		Persona other = (Persona) obj;
		return Objects.equals(contrasena, other.contrasena) && Objects.equals(correo, other.correo)
				&& Objects.equals(documento, other.documento) && Objects.equals(id, other.id)
				&& Objects.equals(nombre, other.nombre) && tipoUsuario == other.tipoUsuario;
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

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getCodigoVerificacion() {
		return codigoVerificacion;
	}

	public void setCodigoVerificacion(String codigoVerificacion) {
		this.codigoVerificacion = codigoVerificacion;
	}

	public boolean isCorreoVerificado() {
		return correoVerificado;
	}

	public void setCorreoVerificado(boolean correoVerificado) {
		this.correoVerificado = correoVerificado;
	}
}
