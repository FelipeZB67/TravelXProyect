package co.edu.unbosque.travelx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import co.edu.unbosque.travelx.dto.PersonaDTO;
import co.edu.unbosque.travelx.entity.Persona;
import co.edu.unbosque.travelx.entity.Persona.TipoUsuario;
import co.edu.unbosque.travelx.repository.PersonaRepository;
import co.edu.unbosque.travelx.service.EmailService;
import co.edu.unbosque.travelx.service.PersonaService;
import static org.mockito.Mockito.doNothing;
import jakarta.mail.MessagingException;

/**
 * Clase PersonaTest.
 */
@ExtendWith(MockitoExtension.class)
class PersonaTest {

	/**
	 * Atributo repositorio personaRepository.
	 */
	@Mock
	private PersonaRepository personaRepository;

	/**
	 * Atributo mapper.
	 */
	@Mock
	private ModelMapper mapper;

	/**
	 * Atributo passwordEncoder.
	 */
	@Mock
	private PasswordEncoder passwordEncoder;

	/**
	 * Atributo emailService.
	 */
	@Mock
	private EmailService emailService;

	/**
	 * Atributo service personaService.
	 */
	@InjectMocks
	private PersonaService personaService;

	/**
	 * Metodo comprobar crear.
	 */
	@Test
	public void comprobarCrear() {

		PersonaDTO dto = new PersonaDTO("Juan Perez", "1001234567", "juan@gmail.com", "Pass123#",
				TipoUsuario.USUARIO);
		Persona entity = new Persona("Juan Perez", "1001234567", "juan@gmail.com", "Pass123#",
				TipoUsuario.USUARIO);

		when(mapper.map(dto, Persona.class)).thenReturn(entity);
		when(personaRepository.findByCorreo(dto.getCorreo())).thenReturn(Optional.empty());
		when(personaRepository.findByDocumento(dto.getDocumento())).thenReturn(Optional.empty());
		when(passwordEncoder.encode(entity.getContrasena())).thenReturn("contrasenaCodificada");

		int resultado = personaService.create(dto);

		assertEquals(0, resultado);
		assertEquals("contrasenaCodificada", entity.getContrasena());
		verify(personaRepository, times(1)).save(entity);
	}

	/**
	 * Metodo comprobar crear con correo repetido.
	 */
	@Test
	public void comprobarCrearCorreoRepetido() {

		PersonaDTO dto = new PersonaDTO("Juan Perez", "1001234567", "juan@gmail.com", "Pass123#",
				TipoUsuario.USUARIO);
		Persona entity = new Persona("Juan Perez", "1001234567", "juan@gmail.com", "Pass123#",
				TipoUsuario.USUARIO);

		when(mapper.map(dto, Persona.class)).thenReturn(entity);
		when(personaRepository.findByCorreo(dto.getCorreo())).thenReturn(Optional.of(entity));

		int resultado = personaService.create(dto);

		assertEquals(2, resultado);
	}

	/**
	 * Metodo comprobar crear administrador con correo no permitido.
	 */
	@Test
	public void comprobarCrearAdministradorCorreoNoPermitido() {

		PersonaDTO dto = new PersonaDTO("Admin", "1001234568", "admin@gmail.com", "Pass123#",
				TipoUsuario.ADMINISTRADOR);
		Persona entity = new Persona("Admin", "1001234568", "admin@gmail.com", "Pass123#",
				TipoUsuario.ADMINISTRADOR);

		when(mapper.map(dto, Persona.class)).thenReturn(entity);
		when(personaRepository.findByCorreo(dto.getCorreo())).thenReturn(Optional.empty());
		when(personaRepository.findByDocumento(dto.getDocumento())).thenReturn(Optional.empty());

		int resultado = personaService.create(dto);

		assertEquals(5, resultado);
	}

	/**
	 * Metodo comprobar eliminar.
	 */
	@Test
	public void comprobarEliminar() {

		Persona entity = new Persona("Ana Ruiz", "1001234569", "ana@gmail.com", "Pass123#", TipoUsuario.USUARIO);
		entity.setId(1L);

		when(personaRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

		int resultado = personaService.deleteById(entity.getId());

		assertEquals(0, resultado);
		verify(personaRepository, times(1)).delete(entity);
	}

	/**
	 * Metodo comprobar eliminar no encontrado.
	 */
	@Test
	public void comprobarEliminarNoEncontrado() {

		when(personaRepository.findById(1L)).thenReturn(Optional.empty());

		int resultado = personaService.deleteById(1L);

		assertEquals(1, resultado);
	}

	/**
	 * Metodo comprobar actualizar.
	 */
	@Test
	public void comprobarActualizar() {

		Persona entity = new Persona("Ana Ruiz", "1001234569", "ana@gmail.com", "Pass123#", TipoUsuario.USUARIO);
		entity.setId(1L);
		PersonaDTO dto = new PersonaDTO("Ana Maria Ruiz", "1001234569", "ana.ruiz@gmail.com", "Nueva123#",
				TipoUsuario.USUARIO);

		when(personaRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
		when(personaRepository.findByCorreo(dto.getCorreo())).thenReturn(Optional.empty());
		when(personaRepository.findByDocumento(dto.getDocumento())).thenReturn(Optional.of(entity));
		when(passwordEncoder.encode(dto.getContrasena())).thenReturn("nuevaCodificada");

		int resultado = personaService.updateById(entity.getId(), dto);

		assertEquals(0, resultado);
		assertEquals("Ana Maria Ruiz", entity.getNombre());
		assertEquals("nuevaCodificada", entity.getContrasena());
		verify(personaRepository, times(1)).save(entity);
	}

	/**
	 * Metodo comprobar actualizar no encontrado.
	 */
	@Test
	public void comprobarActualizarNoEncontrado() {

		PersonaDTO dto = new PersonaDTO("Ana Maria Ruiz", "1001234569", "ana.ruiz@gmail.com", "Nueva123#",
				TipoUsuario.USUARIO);

		when(personaRepository.findById(1L)).thenReturn(Optional.empty());

		int resultado = personaService.updateById(1L, dto);

		assertEquals(4, resultado);
	}

	/**
	 * Metodo comprobar obtener todos.
	 */
	@Test
	public void comprobarGetAll() {

		Persona entity = new Persona("Ana Ruiz", "1001234569", "ana@gmail.com", "Pass123#", TipoUsuario.USUARIO);
		PersonaDTO dto = new PersonaDTO("Ana Ruiz", "1001234569", "ana@gmail.com", "Pass123#",
				TipoUsuario.USUARIO);

		when(personaRepository.findAll()).thenReturn(Arrays.asList(entity));
		when(mapper.map(entity, PersonaDTO.class)).thenReturn(dto);

		assertEquals(1, personaService.getAll().size());
	}

	/**
	 * Metodo comprobar obtener administradores.
	 */
	@Test
	public void comprobarGetAllAdmin() {

		Persona admin = new Persona("Admin", "1001234570", "admin@unbosque.edu.co", "Pass123#",
				TipoUsuario.ADMINISTRADOR);
		Persona usuario = new Persona("Usuario", "1001234571", "usuario@gmail.com", "Pass123#",
				TipoUsuario.USUARIO);
		PersonaDTO adminDto = new PersonaDTO("Admin", "1001234570", "admin@unbosque.edu.co", "Pass123#",
				TipoUsuario.ADMINISTRADOR);
		PersonaDTO usuarioDto = new PersonaDTO("Usuario", "1001234571", "usuario@gmail.com", "Pass123#",
				TipoUsuario.USUARIO);

		when(personaRepository.findAll()).thenReturn(Arrays.asList(admin, usuario));
		when(mapper.map(admin, PersonaDTO.class)).thenReturn(adminDto);
		when(mapper.map(usuario, PersonaDTO.class)).thenReturn(usuarioDto);

		assertEquals(1, personaService.getAllAdmin().size());
	}

	/**
	 * Metodo comprobar buscar por id.
	 */
	@Test
	public void comprobarGetById() {

		Persona entity = new Persona("Ana Ruiz", "1001234569", "ana@gmail.com", "Pass123#", TipoUsuario.USUARIO);
		PersonaDTO dto = new PersonaDTO("Ana Ruiz", "1001234569", "ana@gmail.com", "Pass123#",
				TipoUsuario.USUARIO);

		when(personaRepository.findById(1L)).thenReturn(Optional.of(entity));
		when(mapper.map(entity, PersonaDTO.class)).thenReturn(dto);

		assertEquals(dto, personaService.getById(1L));
	}

	/**
	 * Metodo comprobar buscar por id no encontrado.
	 */
	@Test
	public void comprobarGetByIdNoEncontrado() {

		when(personaRepository.findById(1L)).thenReturn(Optional.empty());

		assertNull(personaService.getById(1L));
	}

	/**
	 * Metodo comprobar validar credenciales.
	 */
	@Test
	public void comprobarValidarCredenciales() {

		Persona entity = new Persona("Ana Ruiz", "1001234569", "ana@gmail.com", "codificada",
				TipoUsuario.USUARIO);

		when(personaRepository.findByCorreo(entity.getCorreo())).thenReturn(Optional.of(entity));
		when(passwordEncoder.matches("Pass123#", entity.getPassword())).thenReturn(true);

		int resultado = personaService.validateCredentials(entity.getCorreo(), "Pass123#");

		assertEquals(0, resultado);
	}

	/**
	 * Metodo comprobar registrar con verificacion.
	 */
	@Test
	public void comprobarRegistrarConVerificacion() throws MessagingException {
	    PersonaDTO dto = new PersonaDTO("Laura Lopez", "1001234572", "laura@gmail.com", "Pass123#",
	            TipoUsuario.USUARIO);
	    Persona entity = new Persona("Laura Lopez", "1001234572", "laura@gmail.com", "Pass123#",
	            TipoUsuario.USUARIO);

	    when(mapper.map(dto, Persona.class)).thenReturn(entity);
	    when(personaRepository.findByNombre(dto.getNombre())).thenReturn(Optional.empty());
	    when(personaRepository.findByCorreo(dto.getCorreo())).thenReturn(Optional.empty());
	    when(personaRepository.findByDocumento(dto.getDocumento())).thenReturn(Optional.empty());
	    when(passwordEncoder.encode(entity.getContrasena())).thenReturn("contrasenaCodificada");
	    doNothing().when(emailService).enviarCodigoVerificacion(anyString(), anyString());

	    int resultado = personaService.registerWithVerification(dto);

	    assertEquals(0, resultado);
	    assertFalse(entity.isEnabled());
	    assertFalse(entity.isCorreoVerificado());
	    verify(personaRepository, times(1)).save(entity);
	    verify(emailService, times(1)).enviarCodigoVerificacion(eq(entity.getCorreo()), anyString());
	}

	/**
	 * Metodo comprobar verificar correo.
	 */
	@Test
	public void comprobarVerificarCorreo() {

		Persona entity = new Persona("Laura Lopez", "1001234572", "laura@gmail.com", "Pass123#",
				TipoUsuario.USUARIO);
		entity.setCodigoVerificacion("123456");

		when(personaRepository.findByCorreo(entity.getCorreo())).thenReturn(Optional.of(entity));

		int resultado = personaService.verifyEmail(entity.getCorreo(), "123456");

		assertEquals(0, resultado);
		assertTrue(entity.isEnabled());
		assertTrue(entity.isCorreoVerificado());
		assertNull(entity.getCodigoVerificacion());
		verify(personaRepository, times(1)).save(entity);
	}

	/**
	 * Metodo comprobar si existe.
	 */
	@Test
	public void comprobarSiExiste() {

		when(personaRepository.existsById(1L)).thenReturn(false);

		boolean resultado = personaService.exist(1L);

		assertFalse(resultado);
	}

	/**
	 * Metodo comprobar conteo.
	 */
	@Test
	public void comprobarCount() {

		when(personaRepository.count()).thenReturn(0L);

		long resultado = personaService.count();

		assertEquals(0L, resultado);
	}
}