package co.edu.unbosque.travelx.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.edu.unbosque.travelx.repository.PersonaRepository;

/**
 * Implementación de {@link UserDetailsService} que carga los datos de autenticación
 * de una persona desde la base de datos usando su correo electrónico.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final PersonaRepository personaRepository;

	public UserDetailsServiceImpl(PersonaRepository personaRepository) {
		this.personaRepository = personaRepository;
	}

	/**
	 * Busca y retorna los detalles de autenticación de una persona según su correo electrónico.
	 *
	 * @param correo correo electrónico usado como identificador de autenticación
	 * @return {@link UserDetails} con los datos de la persona encontrada
	 * @throws UsernameNotFoundException si no existe ninguna persona con el correo indicado
	 */
	@Override
	public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
		return personaRepository
				.findByCorreo(correo)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));
	}
}