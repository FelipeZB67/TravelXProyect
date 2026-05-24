package co.edu.unbosque.travelx.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.travelx.dto.PersonaDTO;
import co.edu.unbosque.travelx.entity.Persona;
import co.edu.unbosque.travelx.entity.Persona.TipoUsuario;
import co.edu.unbosque.travelx.repository.PersonaRepository;

/**
 * Servicio que gestiona las operaciones sobre personas en el sistema,
 * incluyendo creación, actualización, eliminación, consulta, registro
 * con verificación de correo y validación de credenciales.
 */
@Service
public class PersonaService implements CRUDOperation<PersonaDTO> {

	@Autowired
	private PersonaRepository personaRepository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmailService emailService;

	public PersonaService() {
	}

	/**
	 * Crea una nueva persona en el sistema validando duplicados de correo
	 * y documento, y codificando la contraseña antes de persistir.
	 *
	 * @param data objeto con los datos de la persona a crear
	 * @return 0 si es exitoso, 2 si el correo ya existe, 3 si el documento ya existe,
	 *         4 si ocurre un error inesperado, 5 si el correo de administrador no es válido
	 */
	@Override
	@Transactional
	public int create(PersonaDTO data) {
		try {
			Persona entity = mapper.map(data, Persona.class);

			if (findUsernameAlreadyTaken(data.getNombre())) {
				return 1;
			}
			if (findCorreoAlreadyTaken(data.getCorreo())) {
				return 2;
			}
			if (findDocumentoAlreadyTaken(data.getDocumento())) {
				return 3;
			}
			if (data.getTipoUsuario() == Persona.TipoUsuario.ADMINISTRADOR
					&& !isAdminEmailAllowed(data.getCorreo())) {
				return 5;
			}

			entity.setContrasena(passwordEncoder.encode(entity.getContrasena()));

			if (data.getTipoUsuario() != null) {
				entity.setTipoUsuario(data.getTipoUsuario());
			} else {
				entity.setTipoUsuario(Persona.TipoUsuario.NINGUNO);
			}

			personaRepository.save(entity);
			return 0;

		} catch (Exception e) {
			e.printStackTrace();
			return 4;
		}
	}

	/**
	 * Actualiza los datos de una persona existente validando duplicados de correo
	 * y documento, y codificando la contraseña si se indica un nuevo valor.
	 *
	 * @param id   identificador de la persona a actualizar
	 * @param data objeto con los nuevos datos de la persona
	 * @return 0 si es exitoso, 2 si el correo ya existe, 3 si el documento ya existe,
	 *         4 si no se encuentra la persona, 5 si ocurre un error inesperado,
	 *         6 si el correo de administrador no es válido
	 */
	@Override
	@Transactional
	public int updateById(Long id, PersonaDTO data) {
		try {
			Optional<Persona> found = personaRepository.findById(id);

			if (found.isEmpty()) {
				return 4;
			}

			Persona temp = found.get();

			Optional<Persona> foundNombre = personaRepository.findByNombre(data.getNombre());
			Optional<Persona> foundCorreo = personaRepository.findByCorreo(data.getCorreo());
			Optional<Persona> foundDocumento = personaRepository.findByDocumento(data.getDocumento());

			if (foundNombre.isPresent() && !foundNombre.get().getId().equals(id)) {
				return 1;
			}
			
			if (foundCorreo.isPresent() && !foundCorreo.get().getId().equals(id)) {
				return 2;
			}
			if (foundDocumento.isPresent() && !foundDocumento.get().getId().equals(id)) {
				return 3;
			}
			if (data.getTipoUsuario() == Persona.TipoUsuario.ADMINISTRADOR
					&& !isAdminEmailAllowed(data.getCorreo())) {
				return 6;
			}

			temp.setNombre(data.getNombre());
			temp.setDocumento(data.getDocumento());
			temp.setCorreo(data.getCorreo());

			if (data.getContrasena() != null
					&& !data.getContrasena().isBlank()
					&& !data.getContrasena().equals("NO_CAMBIAR_PASSWORD")) {
				temp.setContrasena(passwordEncoder.encode(data.getContrasena()));
			}

			if (data.getTipoUsuario() != null) {
				temp.setTipoUsuario(data.getTipoUsuario());
			}

			personaRepository.save(temp);
			return 0;

		} catch (Exception e) {
			e.printStackTrace();
			return 5;
		}
	}

	/**
	 * Elimina una persona del sistema según su ID.
	 *
	 * @param id identificador de la persona a eliminar
	 * @return 0 si se eliminó exitosamente, 1 si no se encontró la persona
	 */
	@Override
	@Transactional
	public int deleteById(Long id) {
		Optional<Persona> found = personaRepository.findById(id);

		if (found.isPresent()) {
			personaRepository.delete(found.get());
			return 0;
		}

		return 1;
	}

	/**
	 * Retorna la lista de todas las personas registradas en el sistema.
	 *
	 * @return lista de {@link PersonaDTO} con todas las personas
	 */
	@Override
	public List<PersonaDTO> getAll() {
		List<Persona> entityList = (List<Persona>) personaRepository.findAll();
		List<PersonaDTO> dtoList = new ArrayList<>();

		entityList.forEach(entity -> {
			PersonaDTO dto = mapper.map(entity, PersonaDTO.class);
			dtoList.add(dto);
		});

		return dtoList;
	}

	/**
	 * Retorna la lista de todas las personas con rol ADMINISTRADOR.
	 *
	 * @return lista de {@link PersonaDTO} filtrada por rol ADMINISTRADOR
	 */
	public List<PersonaDTO> getAllAdmin() {
		List<Persona> entityList = (List<Persona>) personaRepository.findAll();
		List<PersonaDTO> dtoList = new ArrayList<>();

		entityList.forEach(entity -> {
			PersonaDTO dto = mapper.map(entity, PersonaDTO.class);
			if (dto.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
				dtoList.add(dto);
			}
		});

		return dtoList;
	}

	/**
	 * Retorna la lista de todas las personas con rol USUARIO.
	 *
	 * @return lista de {@link PersonaDTO} filtrada por rol USUARIO
	 */
	public List<PersonaDTO> getAllUsuario() {
		List<Persona> entityList = (List<Persona>) personaRepository.findAll();
		List<PersonaDTO> dtoList = new ArrayList<>();

		entityList.forEach(entity -> {
			PersonaDTO dto = mapper.map(entity, PersonaDTO.class);
			if (dto.getTipoUsuario() == TipoUsuario.USUARIO) {
				dtoList.add(dto);
			}
		});

		return dtoList;
	}

	/**
	 * Retorna la lista de todas las personas con rol NINGUNO.
	 *
	 * @return lista de {@link PersonaDTO} filtrada por rol NINGUNO
	 */
	public List<PersonaDTO> getAllNinguno() {
		List<Persona> entityList = (List<Persona>) personaRepository.findAll();
		List<PersonaDTO> dtoList = new ArrayList<>();

		entityList.forEach(entity -> {
			PersonaDTO dto = mapper.map(entity, PersonaDTO.class);
			if (dto.getTipoUsuario() == TipoUsuario.NINGUNO) {
				dtoList.add(dto);
			}
		});

		return dtoList;
	}

	@Override
	public boolean exist(Long id) {
		return personaRepository.existsById(id);
	}

	@Override
	public long count() {
		return personaRepository.count();
	}

	/**
	 * Busca y retorna una persona según su ID.
	 *
	 * @param id identificador de la persona a buscar
	 * @return {@link PersonaDTO} con los datos de la persona, o {@code null} si no existe
	 */
	public PersonaDTO getById(Long id) {
		Optional<Persona> found = personaRepository.findById(id);

		if (found.isPresent()) {
			return mapper.map(found.get(), PersonaDTO.class);
		}

		return null;
	}

	/**
	 * Verifica si el nombre de usuario de una entidad {@link Persona} ya está registrado.
	 *
	 * @param newUser entidad cuyo nombre de usuario se desea verificar
	 * @return {@code true} si el nombre ya existe, {@code false} en caso contrario
	 */
	public boolean findUsernameAlreadyTaken(Persona newUser) {
		Optional<Persona> found = personaRepository.findByNombre(newUser.getUsername());
		return found.isPresent();
	}

	/**
	 * Verifica si un nombre de usuario ya está registrado en el sistema.
	 *
	 * @param nombre nombre de usuario a verificar
	 * @return {@code true} si el nombre ya existe, {@code false} en caso contrario
	 */
	public boolean findUsernameAlreadyTaken(String nombre) {
		Optional<Persona> found = personaRepository.findByNombre(nombre);
		return found.isPresent();
	}

	/**
	 * Verifica si un correo electrónico ya está registrado en el sistema.
	 *
	 * @param correo correo electrónico a verificar
	 * @return {@code true} si el correo ya existe, {@code false} en caso contrario
	 */
	public boolean findCorreoAlreadyTaken(String correo) {
		Optional<Persona> found = personaRepository.findByCorreo(correo);
		return found.isPresent();
	}

	/**
	 * Verifica si un número de documento ya está registrado en el sistema.
	 *
	 * @param documento número de documento a verificar
	 * @return {@code true} si el documento ya existe, {@code false} en caso contrario
	 */
	public boolean findDocumentoAlreadyTaken(String documento) {
		Optional<Persona> found = personaRepository.findByDocumento(documento);
		return found.isPresent();
	}

	/**
	 * Valida las credenciales de una persona comparando la contraseña
	 * ingresada con la almacenada de forma cifrada.
	 *
	 * @param correo     correo electrónico de la persona
	 * @param contrasena contraseña en texto plano a validar
	 * @return 0 si las credenciales son válidas, 1 si son incorrectas o no existe la persona
	 */
	public int validateCredentials(String correo, String contrasena) {
		Optional<Persona> personaOpt = personaRepository.findByCorreo(correo);

		if (personaOpt.isPresent()) {
			Persona user = personaOpt.get();

			if (passwordEncoder.matches(contrasena, user.getPassword())) {
				return 0;
			}
		}

		return 1;
	}

	/**
	 * Registra una nueva persona en el sistema generando un código de verificación
	 * y enviándolo al correo electrónico indicado. La cuenta queda deshabilitada
	 * hasta que se verifique el correo.
	 *
	 * @param data objeto con los datos de la persona a registrar
	 * @return 0 si es exitoso, 1 si el nombre ya existe, 2 si el correo ya existe,
	 *         3 si el documento ya existe, 4 si ocurre un error inesperado,
	 *         5 si el correo de administrador no es válido
	 * @throws org.springframework.mail.MailException si falla el envío del correo de verificación
	 */
	@Transactional
	public int registerWithVerification(PersonaDTO data) {
		try {
			Persona entity = mapper.map(data, Persona.class);

			if (findUsernameAlreadyTaken(data.getNombre())) {
				return 1;
			}
			if (findCorreoAlreadyTaken(data.getCorreo())) {
				return 2;
			}
			if (findDocumentoAlreadyTaken(data.getDocumento())) {
				return 3;
			}
			if (data.getTipoUsuario() == Persona.TipoUsuario.ADMINISTRADOR && !isAdminEmailAllowed(data.getCorreo())) {
				return 5;
			}

			String codigo = String.valueOf((int) (Math.random() * 900000) + 100000);

			entity.setContrasena(passwordEncoder.encode(entity.getContrasena()));

			if (data.getTipoUsuario() != null) {
				entity.setTipoUsuario(data.getTipoUsuario());
			}
			if (data.getTipoUsuario() != null) {
				entity.setTipoUsuario(data.getTipoUsuario());
			} else {
				entity.setTipoUsuario(Persona.TipoUsuario.NINGUNO);
			}

			entity.setEnabled(false);
			entity.setCorreoVerificado(false);
			entity.setCodigoVerificacion(codigo);

			personaRepository.save(entity);

			emailService.enviarCodigoVerificacion(entity.getCorreo(), codigo);

			return 0;

		} catch (org.springframework.mail.MailException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			return 4;
		}
	}

	/**
	 * Verifica el correo electrónico de una persona usando el código enviado
	 * al momento del registro, habilitando la cuenta si el código es correcto.
	 *
	 * @param correo correo electrónico de la persona a verificar
	 * @param codigo código de verificación ingresado
	 * @return 0 si la verificación es exitosa, 1 si no existe la cuenta,
	 *         2 si el correo ya estaba verificado, 3 si el código es inválido
	 */
	public int verifyEmail(String correo, String codigo) {
		Optional<Persona> found = personaRepository.findByCorreo(correo);
		if (found.isEmpty()) {
			return 1;
		}
		Persona persona = found.get();
		if (persona.isCorreoVerificado()) {
			return 2;
		}
		if (persona.getCodigoVerificacion() == null || !persona.getCodigoVerificacion().equals(codigo)) {
			return 3;
		}
		persona.setCorreoVerificado(true);
		persona.setEnabled(true);
		persona.setCodigoVerificacion(null);
		personaRepository.save(persona);
		return 0;
	}

	/**
	 * Busca y retorna una persona según su correo electrónico.
	 *
	 * @param correo correo electrónico de la persona a buscar
	 * @return {@link PersonaDTO} con los datos de la persona, o {@code null} si no existe
	 */
	public PersonaDTO getByCorreo(String correo) {
		Optional<Persona> found = personaRepository.findByCorreo(correo);

		if (found.isPresent()) {
			return mapper.map(found.get(), PersonaDTO.class);
		}

		return null;
	}

	private static final String DOMINIO_ADMIN = "@unbosque.edu.co";

	/**
	 * Verifica si un correo electrónico pertenece al dominio permitido
	 * para el registro de administradores.
	 *
	 * @param correo correo electrónico a validar
	 * @return {@code true} si el correo termina en {@code @unbosque.edu.co}
	 */
	private boolean isAdminEmailAllowed(String correo) {
		return correo != null && correo.toLowerCase().endsWith(DOMINIO_ADMIN);
	}
}