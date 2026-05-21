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

			if (data.getContrasena() != null && !data.getContrasena().isBlank()) {
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

	public PersonaDTO getById(Long id) {
		Optional<Persona> found = personaRepository.findById(id);

		if (found.isPresent()) {
			return mapper.map(found.get(), PersonaDTO.class);
		}

		return null;
	}

	public boolean findUsernameAlreadyTaken(Persona newUser) {
		Optional<Persona> found = personaRepository.findByNombre(newUser.getUsername());
		return found.isPresent();
	}

	public boolean findUsernameAlreadyTaken(String nombre) {
		Optional<Persona> found = personaRepository.findByNombre(nombre);
		return found.isPresent();
	}

	public boolean findCorreoAlreadyTaken(String correo) {
		Optional<Persona> found = personaRepository.findByCorreo(correo);
		return found.isPresent();
	}

	public boolean findDocumentoAlreadyTaken(String documento) {
		Optional<Persona> found = personaRepository.findByDocumento(documento);
		return found.isPresent();
	}

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

	public PersonaDTO getByCorreo(String correo) {
		Optional<Persona> found = personaRepository.findByCorreo(correo);

		if (found.isPresent()) {
			return mapper.map(found.get(), PersonaDTO.class);
		}

		return null;
	}

	private static final String DOMINIO_ADMIN = "@unbosque.edu.co";

	private boolean isAdminEmailAllowed(String correo) {
		return correo != null && correo.toLowerCase().endsWith(DOMINIO_ADMIN);
	}
}