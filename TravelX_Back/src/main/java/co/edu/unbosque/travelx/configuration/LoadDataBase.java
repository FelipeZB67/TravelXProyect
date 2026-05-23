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

@Configuration
public class LoadDataBase {

	private static final Logger Log = LoggerFactory.getLogger(LoadDataBase.class);

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