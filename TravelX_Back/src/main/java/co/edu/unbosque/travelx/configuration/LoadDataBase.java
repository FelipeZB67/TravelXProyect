package co.edu.unbosque.travelx.configuration;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import co.edu.unbosque.travelx.entity.Persona;
import co.edu.unbosque.travelx.repository.PersonaRepository;

/**
 * Clase de configuración que precarga datos iniciales en la base de datos al
 * arrancar la aplicación, creando usuarios por defecto si no existen.
 */
@Configuration
public class LoadDataBase {

	@Value("${travelx.seed.admin.correo}")
	private String adminCorreo;

	@Value("${travelx.seed.admin.contrasena}")
	private String adminContrasena;

	@Value("${travelx.seed.usuario.correo}")
	private String usuarioCorreo;

	@Value("${travelx.seed.usuario.contrasena}")
	private String usuarioContrasena;

	@Value("${travelx.seed.ninguno.correo}")
	private String ningunoCorreo;

	@Value("${travelx.seed.ninguno.contrasena}")
	private String ningunoContrasena;
	private static final Logger Log = LoggerFactory.getLogger(LoadDataBase.class);

	@Bean
	CommandLineRunner initDataBase(PersonaRepository personaRepo, PasswordEncoder encoder) {
		return args -> {

			Optional<Persona> foundAdmin = personaRepo.findByNombre("FelipeAdmin");

			if (foundAdmin.isPresent()) {
				Log.info("El admin ya existe, omitiendo creación...");
			} else {
				Persona admin = crearPersonaPrecargada("FelipeAdmin", "1012976249", adminCorreo,
						encoder.encode(adminContrasena), Persona.TipoUsuario.ADMINISTRADOR);
				personaRepo.save(admin);
				Log.info("Precargado admin con éxito");
			}

			Optional<Persona> foundUser = personaRepo.findByNombre("FelipeUsuario");

			if (foundUser.isPresent()) {
				Log.info("El usuario normal ya existe, omitiendo creación...");
			} else {
				Persona user = crearPersonaPrecargada("FelipeUsuario", "1012976240", usuarioCorreo,
						encoder.encode(usuarioContrasena), Persona.TipoUsuario.USUARIO);
				personaRepo.save(user);
				Log.info("Precargado usuario normal con éxito");
			}

			Optional<Persona> foundNinguno = personaRepo.findByNombre("ninguno");

			if (foundNinguno.isPresent()) {
				Log.info("El usuario sin rol ya existe, omitiendo creación...");
			} else {
				Persona ninguno = crearPersonaPrecargada("ninguno", "1000000003", ningunoCorreo,
						encoder.encode(ningunoContrasena), Persona.TipoUsuario.NINGUNO);
				personaRepo.save(ninguno);
				Log.info("Precargado usuario sin rol con éxito");
			}
		};
	}

	private Persona crearPersonaPrecargada(String nombre, String documento, String correo, String contrasena,
			Persona.TipoUsuario tipoUsuario) {
		Persona persona = new Persona(nombre, documento, correo, contrasena, tipoUsuario);
		persona.setAccountNonExpired(true);
		persona.setAccountNonLocked(true);
		persona.setCredentialsNonExpired(true);
		persona.setEnabled(true);
		persona.setCorreoVerificado(true);
		persona.setCodigoVerificacion(null);
		return persona;
	}
}