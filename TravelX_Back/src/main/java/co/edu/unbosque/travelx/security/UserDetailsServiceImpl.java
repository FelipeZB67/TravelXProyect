package co.edu.unbosque.travelx.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.edu.unbosque.travelx.repository.PersonaRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final PersonaRepository personaRepository;

	public UserDetailsServiceImpl(PersonaRepository personaRepository) {
		this.personaRepository = personaRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
		return personaRepository
				.findByCorreo(correo)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));
	}
}