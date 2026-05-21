package co.edu.unbosque.travelx.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.travelx.dto.ReservaInternacionalDTO;
import co.edu.unbosque.travelx.entity.ReservaInternacional;
import co.edu.unbosque.travelx.repository.ReservaInternacionalRepository;

@Service
public class ReservaInternacionalService implements CRUDOperation<ReservaInternacionalDTO> {

	@Autowired
	private ReservaInternacionalRepository reservaInternacionalRepository;

	@Autowired
	private ModelMapper mapper;

	public ReservaInternacionalService() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int create(ReservaInternacionalDTO data) {
		try {
			ReservaInternacional entity = mapper.map(data, ReservaInternacional.class);
			reservaInternacionalRepository.save(entity);
			return 0;
		} catch (Exception e) {
			return 1;
		}
	}

	@Override
	public int updateById(Long id, ReservaInternacionalDTO data) {
		try {
			Optional<ReservaInternacional> found = reservaInternacionalRepository.findById(id);
			if (found.isPresent()) {
				ReservaInternacional temp = found.get();

				temp.setCiudadDestino(data.getCiudadDestino());
				temp.setCiudadOrigen(data.getCiudadOrigen());
				temp.setFechaFin(data.getFechaFin());
				temp.setFechaInicio(data.getFechaInicio());
				temp.setHotel(data.getHotel());
				temp.setPrecioHospedaje(data.getPrecioHospedaje());
				temp.setPrecioTransporte(data.getPrecioTransporte());
				temp.setMetodoTransporte(data.getMetodoTransporte());

				temp.setPaisDestino(data.getPaisDestino());
				temp.setPaisOrigen(data.getPaisOrigen());
				temp.setRequiereVisa(data.isRequiereVisa());
				reservaInternacionalRepository.save(temp);
				return 0;
			}
			return 1;
		} catch (Exception e) {
			return 2;
		}
	}

	@Override
	public int deleteById(Long id) {
		Optional<ReservaInternacional> found = reservaInternacionalRepository.findById(id);
		if (found.isPresent()) {
			reservaInternacionalRepository.delete(found.get());
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public List<ReservaInternacionalDTO> getAll() {
		List<ReservaInternacional> entityList = (List<ReservaInternacional>) reservaInternacionalRepository.findAll();
		List<ReservaInternacionalDTO> dtoList = new ArrayList<>();
		entityList.forEach((entity) -> {
			ReservaInternacionalDTO dto = mapper.map(entity, ReservaInternacionalDTO.class);
			dtoList.add(dto);
		});
		return dtoList;
	}

	@Override
	public boolean exist(Long id) {
		return reservaInternacionalRepository.existsById(id);
	}

	@Override
	public long count() {
		return reservaInternacionalRepository.count();
	}

}
