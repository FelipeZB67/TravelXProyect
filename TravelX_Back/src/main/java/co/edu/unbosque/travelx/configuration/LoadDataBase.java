package co.edu.unbosque.travelx.configuration;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import co.edu.unbosque.travelx.entity.Persona;
import co.edu.unbosque.travelx.repository.PersonaRepository;

/**
 * Clase de configuración que precarga datos iniciales en la base de datos
 * al arrancar la aplicación, creando usuarios por defecto si no existen.
 */
@Configuration
public class LoadDataBase {

	private static final Logger Log = LoggerFactory.getLogger(LoadDataBase.class);

	/**
     * Inicializa la base de datos con tres usuarios por defecto: un administrador,
     * un usuario normal y un usuario sin rol. Si alguno ya existe, omite su creación.
     *
     * @param personaRepo repositorio de personas usado para buscar y guardar usuarios
     * @param encoder     codificador de contraseñas usado para cifrar las claves por defecto
     * @return un {@link CommandLineRunner} que ejecuta la lógica de precarga al inicio
     */
	@Bean
	CommandLineRunner initDataBase(PersonaRepository personaRepo, PasswordEncoder encoder) {
		return args -> {

			Optional<Persona> foundAdmin = personaRepo.findByNombre("admini");

			if (foundAdmin.isPresent()) {
				Log.info("El admin ya existe, omitiendo creación...");
			} else {
				personaRepo.save(new Persona("admini", "0000000001", "admin@unbosque.edu.co", encoder.encode("1234567890"),
						Persona.TipoUsuario.ADMINISTRADOR));

				Log.info("Precargado admin con éxito");
			}

			Optional<Persona> foundUser = personaRepo.findByNombre("user");

			if (foundUser.isPresent()) {
				Log.info("El usuario normal ya existe, omitiendo creación...");
			} else {
				personaRepo.save(new Persona("user", "1000000002", "user@travelx.com", encoder.encode("1234567890"),
						Persona.TipoUsuario.USUARIO));

				Log.info("Precargado usuario normal con éxito");
			}

			Optional<Persona> foundNinguno = personaRepo.findByNombre("ninguno");

			if (foundNinguno.isPresent()) {
				Log.info("El usuario sin rol ya existe, omitiendo creación...");
			} else {
				personaRepo.save(new Persona("ninguno", "1000000003", "ninguno@travelx.com",
						encoder.encode("1234567890"), Persona.TipoUsuario.NINGUNO));

				Log.info("Precargado usuario sin rol con éxito");
			}
		};
	}
}