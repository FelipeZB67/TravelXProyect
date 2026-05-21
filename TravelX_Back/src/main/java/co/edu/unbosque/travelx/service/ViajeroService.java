package co.edu.unbosque.travelx.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.unbosque.travelx.dto.ViajeroDTO;
import co.edu.unbosque.travelx.entity.Viajero;
import co.edu.unbosque.travelx.repository.ViajeroRepository;

@Service
public class ViajeroService implements CRUDOperation<ViajeroDTO>{
	
	@Autowired
	private ViajeroRepository viajeroRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public ViajeroService() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean findDocumentoAlreadyTaken(String pasaporte) {
		Optional<Viajero> found = viajeroRepository.findByPasaporte(pasaporte);
		return found.isPresent();
	}



	@Override
	public int create(ViajeroDTO data) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public int updateById(Long id, ViajeroDTO data) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public int deleteById(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public List<ViajeroDTO> getAll() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public boolean exist(Long id) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

}
