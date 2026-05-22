package co.edu.unbosque.travelx.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.travelx.dto.MascotaDTO;
import co.edu.unbosque.travelx.dto.ReservaInternacionalDTO;
import co.edu.unbosque.travelx.dto.ViajeroDTO;
import co.edu.unbosque.travelx.entity.Mascota;
import co.edu.unbosque.travelx.entity.Reserva;
import co.edu.unbosque.travelx.entity.ReservaInternacional;
import co.edu.unbosque.travelx.entity.Viajero;
import co.edu.unbosque.travelx.repository.PersonaRepository;
import co.edu.unbosque.travelx.repository.ReservaInternacionalRepository;

@Service
public class ReservaInternacionalService implements CRUDOperation<ReservaInternacionalDTO>{

	@Autowired
	private ReservaInternacionalRepository resInterRepo;
	
	@Autowired
	private PersonaRepository userRepo;
	
	@Autowired
	private ModelMapper mapper;
	@Override
	public int create(ReservaInternacionalDTO data) {
		ReservaInternacional entity = mapper.map(data, ReservaInternacional.class);
		resInterRepo.save(entity);
		return 0;
	}

	@Override
	public int updateById(Long id, ReservaInternacionalDTO data) {
		Optional<ReservaInternacional> find = resInterRepo.findById(id);

		if (find.isPresent()) {
			ReservaInternacional temp = find.get();

			if (data.getMetodoTransporte() != null)
				temp.setMetodoTransporte(data.getMetodoTransporte());
			
			temp.setFechaInicio(data.getFechaInicio());
			temp.setFechaFin(data.getFechaFin());
			temp.setCiudadOrigen(data.getCiudadOrigen());
			temp.setCiudadDestino(data.getCiudadDestino());
			temp.setHotel(data.getHotel());
			temp.setPaisOrigen(data.getPaisOrigen());
			temp.setPaisDestino(data.getPaisDestino());
			
			resInterRepo.save(temp);
			return 0;
		}
		return 1;
	}

	@Override
	public int deleteById(Long id) {
		Optional<ReservaInternacional> find = resInterRepo.findById(id);
		
		if(find.isPresent()) {
			resInterRepo.deleteById(id);
			return 0;
		}
		return 1;
	}

	@Override
	public List<ReservaInternacionalDTO> getAll() {
		List<ReservaInternacional> entityList = (List<ReservaInternacional>)resInterRepo.findAll();
		List<ReservaInternacionalDTO> dtoList = new ArrayList<>();
		
		entityList.forEach((entity) -> {
			ReservaInternacionalDTO dto = mapper.map(dtoList, ReservaInternacionalDTO.class);
			
			List<Mascota> entityList2 = entity.getListaMascotas();
			List<MascotaDTO> dtoList2 = new ArrayList<>();
			
			entityList2.forEach((mascota) -> {
				MascotaDTO dto2 = mapper.map(mascota, MascotaDTO.class);
				dtoList2.add(dto2);
			});
			
			dto.setListaMascotas(dtoList2);
			
			List<Viajero> entityList3 = entity.getListaViajeros();
			List<ViajeroDTO> dtoList3 = new ArrayList<>();
			
			entityList3.forEach((viajero) ->{
				ViajeroDTO dto3 = mapper.map(viajero, ViajeroDTO.class);
				dtoList3.add(dto3);
			});
			
			dto.setListaViajeros(dtoList3);
			
			dtoList.add(dto);
		});
		return dtoList;
	}

	public List<ReservaInternacionalDTO> getListaReservasInternacionalesIdCliente(Long idCliente){
		List<Reserva> reservas = userRepo.findById(idCliente).get().getListaReservas();
		List<ReservaInternacional> entityList = new ArrayList<>();
		
		reservas.forEach((reserva)->{
			if(reserva instanceof ReservaInternacional) {
				entityList.add((ReservaInternacional)reserva);
			}
		});
		
		List<ReservaInternacionalDTO> dtoList = new ArrayList<>();
		
		entityList.forEach((entity) -> {
			ReservaInternacionalDTO dto = mapper.map(entity, ReservaInternacionalDTO.class);
			
			List<Mascota> entityList2 = entity.getListaMascotas();
			List<MascotaDTO> dtoList2 = new ArrayList<>();
			
			entityList2.forEach((mascota) -> {
				MascotaDTO dto2 = mapper.map(mascota, MascotaDTO.class);
				dtoList2.add(dto2);
			});
			
			dto.setListaMascotas(dtoList2);
			
			List<Viajero> entityList3 = entity.getListaViajeros();
			List<ViajeroDTO> dtoList3 = new ArrayList<>();
			
			entityList3.forEach((viajero) ->{
				ViajeroDTO dto3 = mapper.map(viajero, ViajeroDTO.class);
				dtoList3.add(dto3);
			});
			
			dto.setListaViajeros(dtoList3);
			
			dtoList.add(dto);
		});
		return dtoList;
	}
	
	@Override
	public boolean exist(Long id) {
		return resInterRepo.existsById(id);
	}

	@Override
	public long count() {
		return resInterRepo.count();
	}
}