package co.edu.unbosque.travelx.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.travelx.dto.ViajeroDTO;
import co.edu.unbosque.travelx.entity.Viajero;
import co.edu.unbosque.travelx.repository.ViajeroRepository;

@Service
public class ViajeroService implements CRUDOperation<ViajeroDTO> {

	@Autowired
	private ViajeroRepository viajeroRepository;

	@Autowired
	private ModelMapper mapper;

	public ViajeroService() {
	}

	public boolean findPasaporteAlreadyTaken(String pasaporte) {
		Optional<Viajero> found = viajeroRepository.findByPasaporte(pasaporte);
		return found.isPresent();
	}

	@Override
	public int create(ViajeroDTO data) {
		try {
			if (findPasaporteAlreadyTaken(data.getPasaporte())) {
				return 1;
			}

			Viajero entity = mapper.map(data, Viajero.class);
			viajeroRepository.save(entity);
			return 0;
		} catch (Exception e) {
			return 2;
		}
	}

	@Override
	public int updateById(Long id, ViajeroDTO data) {
		try {
			Optional<Viajero> found = viajeroRepository.findById(id);

			if (found.isPresent()) {
				Viajero temp = found.get();

				Optional<Viajero> foundPasaporte = viajeroRepository.findByPasaporte(data.getPasaporte());

				if (foundPasaporte.isPresent() && !foundPasaporte.get().getId().equals(id)) {
					return 2;
				}

				temp.setNombre(data.getNombre());
				temp.setPasaporte(data.getPasaporte());
				temp.setFechaNacimiento(data.getFechaNacimiento());

				viajeroRepository.save(temp);
				return 0;
			}

			return 1;
		} catch (Exception e) {
			return 3;
		}
	}

	@Override
	public int deleteById(Long id) {
		Optional<Viajero> found = viajeroRepository.findById(id);

		if (found.isPresent()) {
			viajeroRepository.delete(found.get());
			return 0;
		}

		return 1;
	}

	@Override
	public List<ViajeroDTO> getAll() {
		List<Viajero> entityList = (List<Viajero>) viajeroRepository.findAll();
		List<ViajeroDTO> dtoList = new ArrayList<>();

		entityList.forEach((entity) -> {
			ViajeroDTO dto = mapper.map(entity, ViajeroDTO.class);
			dtoList.add(dto);
		});

		return dtoList;
	}

	public ViajeroDTO getById(Long id) {
		Optional<Viajero> found = viajeroRepository.findById(id);

		if (found.isPresent()) {
			return mapper.map(found.get(), ViajeroDTO.class);
		}

		return null;
	}

	@Override
	public boolean exist(Long id) {
		return viajeroRepository.existsById(id);
	}

	@Override
	public long count() {
		return viajeroRepository.count();
	}
}