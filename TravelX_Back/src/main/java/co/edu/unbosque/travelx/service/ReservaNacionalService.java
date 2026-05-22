package co.edu.unbosque.travelx.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import co.edu.unbosque.travelx.dto.MascotaDTO;
import co.edu.unbosque.travelx.dto.ReservaNacionalDTO;
import co.edu.unbosque.travelx.dto.ViajeroDTO;
import co.edu.unbosque.travelx.entity.Mascota;
import co.edu.unbosque.travelx.entity.Reserva;
import co.edu.unbosque.travelx.entity.ReservaNacional;
import co.edu.unbosque.travelx.entity.Viajero;
import co.edu.unbosque.travelx.repository.PersonaRepository;
import co.edu.unbosque.travelx.repository.ReservaNacionalRepository;

public class ReservaNacionalService implements CRUDOperation<ReservaNacionalDTO>{

	@Autowired
	private ReservaNacionalRepository resNacionalRepo;
	
	@Autowired
	private PersonaRepository userRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public int create(ReservaNacionalDTO data) {
		ReservaNacional entity = mapper.map(data, ReservaNacional.class);
		resNacionalRepo.save(entity);
		return 0;
	}

	@Override
	public int updateById(Long id, ReservaNacionalDTO data) {
		Optional<ReservaNacional> find = resNacionalRepo.findById(id);

		if (find.isPresent()) {
			ReservaNacional temp = find.get();

			if (data.getMetodoTransporte() != null)
				temp.setMetodoTransporte(data.getMetodoTransporte());
			
			temp.setFechaInicio(data.getFechaInicio());
			temp.setFechaFin(data.getFechaFin());
			temp.setCiudadOrigen(data.getCiudadOrigen());
			temp.setCiudadDestino(data.getCiudadDestino());
			temp.setHotel(data.getHotel());
			
			resNacionalRepo.save(temp);
			return 0;
		}
		return 1;
	}

	@Override
	public int deleteById(Long id) {
		Optional<ReservaNacional> find = resNacionalRepo.findById(id);
		
		if(find.isPresent()) {
			resNacionalRepo.deleteById(id);
			return 0;
		}
		return 1;
	}

	@Override
	public List<ReservaNacionalDTO> getAll() {
		List<ReservaNacional> entityList = (List<ReservaNacional>)resNacionalRepo.findAll();
		List<ReservaNacionalDTO> dtoList = new ArrayList<>();
		
		entityList.forEach((entity) -> {
			ReservaNacionalDTO dto = mapper.map(dtoList, ReservaNacionalDTO.class);
			
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

	public List<ReservaNacionalDTO> getListaReservasNacionalesIdCliente(Long idCliente) {
		List<Reserva> reservas = userRepo.findById(idCliente).get().getListaReservas();
		List<ReservaNacional> entityList = new ArrayList<>();

		reservas.forEach((reserva) -> {
			if (reserva instanceof ReservaNacional) {
				entityList.add((ReservaNacional) reserva);
			}
		});

		List<ReservaNacionalDTO> dtoList = new ArrayList<>();

		entityList.forEach((entity) -> {
			ReservaNacionalDTO dto = mapper.map(entity, ReservaNacionalDTO.class);

			List<Mascota> entityList2 = entity.getListaMascotas();
			List<MascotaDTO> dtoList2 = new ArrayList<>();

			entityList2.forEach((mascota) -> {
				MascotaDTO dto2 = mapper.map(mascota, MascotaDTO.class);
				dtoList2.add(dto2);
			});

			dto.setListaMascotas(dtoList2);

			List<Viajero> entityList3 = entity.getListaViajeros();
			List<ViajeroDTO> dtoList3 = new ArrayList<>();

			entityList3.forEach((viajero) -> {
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
		return resNacionalRepo.existsById(id);
	}

	@Override
	public long count() {
		return resNacionalRepo.count();
	}
}