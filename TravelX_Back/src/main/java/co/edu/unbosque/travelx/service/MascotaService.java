package co.edu.unbosque.travelx.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import co.edu.unbosque.travelx.dto.MascotaDTO;
import co.edu.unbosque.travelx.entity.Mascota;
import co.edu.unbosque.travelx.repository.MascotaRepository;

@Service
public class MascotaService implements CRUDOperation<MascotaDTO>{

	@Autowired
	private MascotaRepository mascotaRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired 
	PasswordEncoder passwordEncoder;
	
	public MascotaService() {
		
	}

	@Override
	public int create(MascotaDTO data) {
		try {
			Mascota entity = mapper.map(data, Mascota.class);
			mascotaRepository.save(entity);
			return 0;
		}catch(Exception e) {
			return 1;
		}
	}

	@Override
	public int updateById(Long id, MascotaDTO data) {
		try {
			Optional<Mascota> found = mascotaRepository.findById(id);
			if(found.isPresent()) {
				Mascota temp = found.get();
				temp.setEspecie(data.getEspecie());
				temp.setFechaNacimiento(data.getFechaNacimiento());
				temp.setRaza(data.getRaza());
				mascotaRepository.save(temp);
				return 0;
			}
			return 1;
		}catch(Exception e) {
			return 2;
		}
	}

	@Override
	public int deleteById(Long id) {
		Optional<Mascota> found = mascotaRepository.findById(id);
		if (found.isPresent()) {
			mascotaRepository.delete(found.get());
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public List<MascotaDTO> getAll() {
		List<Mascota> entityList = (List<Mascota>) mascotaRepository.findAll();
		List<MascotaDTO> dtoList = new ArrayList<>();
		entityList.forEach((entity) -> {
			MascotaDTO dto = mapper.map(entity, MascotaDTO.class);
			dtoList.add(dto);
		});
		return dtoList;
	}

	@Override
	public boolean exist(Long id) {
		return mascotaRepository.existsById(id);
	}

	@Override
	public long count() {
		return mascotaRepository.count();
	}
}
