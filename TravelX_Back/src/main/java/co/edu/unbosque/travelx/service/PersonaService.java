package co.edu.unbosque.travelx.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.unbosque.travelx.dto.PersonaDTO;
import co.edu.unbosque.travelx.entity.Persona;
import co.edu.unbosque.travelx.repository.PersonaRepository;

@Service
public class PersonaService implements CRUDOperation<PersonaDTO> {

	@Autowired
	private PersonaRepository personaRepository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public PersonaService() {
		// TODO Auto-generated constructor stub
	}

	@Override
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
			entity.setContrasena(passwordEncoder.encode(entity.getContrasena()));
			if (data.getTipoUsuario() != null) {
				entity.setTipoUsuario(data.getTipoUsuario());
			}
			personaRepository.save(entity);
			return 0;

		} catch (Exception e) {
			e.printStackTrace();
			return 4;
		}
	}

	@Override
	public int updateById(Long id, PersonaDTO data) {
		try {
			Optional<Persona> found = personaRepository.findById(id);
			Optional<Persona> foundNombre = personaRepository.findByNombre(data.getNombre());
			Optional<Persona> foundCorreo = personaRepository.findByCorreo(data.getCorreo());
			Optional<Persona> foundDocumento = personaRepository.findByDocumento(data.getDocumento());

			if (found.isPresent() && !foundNombre.isPresent() && !foundCorreo.isPresent()
					&& !foundDocumento.isPresent()) {
				Persona temp = found.get();
				temp.setNombre(data.getNombre());
				temp.setDocumento(data.getDocumento());
				temp.setCorreo(data.getCorreo());
				temp.setContrasena(data.getContrasena());
				if (data.getTipoUsuario() != null) {
					data.setTipoUsuario(data.getTipoUsuario());
				}
				personaRepository.save(temp);
				return 0;
			}
			if (found.isPresent() && foundNombre.isPresent()) {
				return 1;
			}
			if (found.isPresent() && foundCorreo.isPresent()) {
				return 2;
			}
			if (found.isPresent() && foundDocumento.isPresent()) {
				return 3;
			}
			if (!found.isPresent()) {
				return 4;
			}
			return 5;
		} catch (Exception e) {
			return 5;
		}
		
	}

	@Override
	public int deleteById(Long id) {
		Optional<Persona> found = personaRepository.findById(id);
		if(found.isPresent()) {
			personaRepository.delete(found.get());
			return 0;
		}else {
			return 1;
		}
	}

	@Override
	public List<PersonaDTO> getAll() {
		List<Persona> entityList = (List<Persona>) personaRepository.findAll();
		List<PersonaDTO> dtoList = new ArrayList<>();
		entityList.forEach((entity)->{
			PersonaDTO dto = mapper.map(entity, PersonaDTO.class);
			dtoList.add(dto);
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

	public boolean findCorreoAlreadyTaken(String correo) {
		Optional<Persona> found = personaRepository.findByCorreo(correo);
		return found.isPresent();
	}

	public boolean findUsernameAlreadyTaken(String nombre) {
		Optional<Persona> found = personaRepository.findByNombre(nombre);
		return found.isPresent();
	}

	public boolean findDocumentoAlreadyTaken(String pasaporte) {
		Optional<Persona> found = personaRepository.findByDocumento(pasaporte);
		return found.isPresent();
	}

	public boolean findUsernameAlreadyTaken(Persona newUser) {
		Optional<Persona> found = personaRepository.findByNombre(newUser.getUsername());
		if (found.isPresent()) {
			return true;
		} else {
			return false;
		}
	}

	public PersonaDTO getById(Long id) {
		Optional<Persona> found = personaRepository.findById(id);
		if (found.isPresent()) {
			return mapper.map(found.get(), PersonaDTO.class);
		} else {
			return null;
		}
	}

}
