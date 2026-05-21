package co.edu.unbosque.travelx.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.travelx.dto.ReservaNacionalDTO;
import co.edu.unbosque.travelx.entity.ReservaNacional;
import co.edu.unbosque.travelx.repository.ReservaNacionalRepository;

@Service
public class ReservaNacionalService implements CRUDOperation<ReservaNacionalDTO> {

	@Autowired
	private ReservaNacionalRepository reservaNacionalRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public int create(ReservaNacionalDTO data) {
		try {
			ReservaNacional entity = mapper.map(data, ReservaNacional.class);
			reservaNacionalRepository.save(entity);
			return 0;
		} catch (Exception e) {
			return 1;
		}
	}

	@Override
	public int updateById(Long id, ReservaNacionalDTO data) {
		try {
			Optional<ReservaNacional> found = reservaNacionalRepository.findById(id);
			if (found.isPresent()) {
				ReservaNacional temp = found.get();
				temp.setCiudadDestino(data.getCiudadDestino());
				temp.setCiudadOrigen(data.getCiudadOrigen());
				temp.setFechaFin(data.getFechaFin());
				temp.setFechaInicio(data.getFechaInicio());
				temp.setHotel(data.getHotel());
				temp.setPrecioHospedaje(data.getPrecioHospedaje());
				temp.setPrecioTransporte(data.getPrecioTransporte());
				if (data.getMetodoTransporte() != null) {
					temp.setMetodoTransporte(data.getMetodoTransporte());
				}
				reservaNacionalRepository.save(temp);
				return 0;
			}
			return 1;
		} catch (Exception e) {
			return 2;
		}
	}

	@Override
	public int deleteById(Long id) {
		Optional<ReservaNacional> found = reservaNacionalRepository.findById(id);
		if(found.isPresent()) {
			reservaNacionalRepository.delete(found.get());
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public List<ReservaNacionalDTO> getAll() {
		List<ReservaNacional> entityList = (List<ReservaNacional>) reservaNacionalRepository.findAll();
		List<ReservaNacionalDTO> dtoList = new ArrayList<>();
		entityList.forEach((entity) ->{
			ReservaNacionalDTO dto = mapper.map(entity, ReservaNacionalDTO.class);
			dtoList.add(dto);
		});
		return dtoList;
	
	}

	@Override
	public boolean exist(Long id) {
		return reservaNacionalRepository.existsById(id);
	}

	@Override
	public long count() {
		return reservaNacionalRepository.count();
	}

}
