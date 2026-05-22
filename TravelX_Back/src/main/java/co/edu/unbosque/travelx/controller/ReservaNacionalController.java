package co.edu.unbosque.travelx.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.travelx.dto.MascotaDTO;
import co.edu.unbosque.travelx.dto.ReservaNacionalDTO;
import co.edu.unbosque.travelx.dto.ViajeroDTO;
import co.edu.unbosque.travelx.entity.Reserva.MetodoTransporte;
import co.edu.unbosque.travelx.service.ReservaNacionalService;

@RestController
@RequestMapping("/reservanacional")
@CrossOrigin(origins = { "http://localhost:8081", "*" })
public class ReservaNacionalController {
	@Autowired
	private ReservaNacionalService nacionalServ;

	@PostMapping("/crear")
	public ResponseEntity<String> createReservaInternacional(@RequestParam Long personaId,
			@RequestParam List<MetodoTransporte> metodoTransporte, @RequestParam LocalDate fechaInicio,
			@RequestParam LocalDate fechaFin, @RequestParam String ciudadOrigen, @RequestParam String ciudadDestino,
			@RequestParam String hotel, @RequestParam List<MascotaDTO> listaMascotas,
			@RequestParam List<ViajeroDTO> listaViajeros, @RequestParam String paisOrigen,
			@RequestParam String paisDestino, @RequestParam boolean requiereVisa) {

		ReservaNacionalDTO dto = new ReservaNacionalDTO(personaId, metodoTransporte, fechaInicio, fechaFin,
				ciudadOrigen, ciudadDestino, hotel, listaMascotas, listaViajeros);
		
		int status = nacionalServ.create(dto);
		
		if(status == 0) {
			return new ResponseEntity<>("Reserva nacional creada",HttpStatus.CREATED);
		}
		
		return new ResponseEntity<>("Error al reservar",HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PutMapping("/actualizar")
	public ResponseEntity<String> actualizarReservaInternacional(@RequestParam Long id, @RequestParam Long personaId,
			@RequestParam List<MetodoTransporte> metodoTransporte, @RequestParam LocalDate fechaInicio,
			@RequestParam LocalDate fechaFin, @RequestParam String ciudadOrigen, @RequestParam String ciudadDestino,
			@RequestParam String hotel, @RequestParam List<MascotaDTO> listaMascotas,
			@RequestParam List<ViajeroDTO> listaViajeros, @RequestParam String paisOrigen,
			@RequestParam String paisDestino, @RequestParam boolean requiereVisa) {

		ReservaNacionalDTO dto = new ReservaNacionalDTO(personaId, metodoTransporte, fechaInicio, fechaFin,
				ciudadOrigen, ciudadDestino, hotel, listaMascotas, listaViajeros);
		
		int status = nacionalServ.updateById(personaId, dto);
		
		if(status == 0) {
			return new ResponseEntity<>("Reserva nacional actualizada",HttpStatus.OK);
		}
		
		if(status == 1) {
			return new ResponseEntity<>("No existe la reserva que busca",HttpStatus.NOT_FOUND);			
		}
		return new ResponseEntity<>("Error al actualizar la reserva",HttpStatus.INTERNAL_SERVER_ERROR);					
	}
	
	@DeleteMapping("/eliminar")
	public ResponseEntity<String> eliminarReservaInternacional(@RequestParam Long id){
		int status = nacionalServ.deleteById(id);
		
		if(status == 0) {
			return new ResponseEntity<>("Reserva eliminada con exito",HttpStatus.OK);								
		}
		
		if(status == 1) {
			return new ResponseEntity<>("No existe la reserva que busca",HttpStatus.NOT_FOUND);								
		}
		return new ResponseEntity<>("Error al eliminar la reserva",HttpStatus.INTERNAL_SERVER_ERROR);							
	}
	
	@GetMapping("/mostrartodo")
	public ResponseEntity<List<ReservaNacionalDTO>> getListaReservas(){
		List<ReservaNacionalDTO> dtoList = nacionalServ.getAll();
		
		if(dtoList != null) {
			return new ResponseEntity<List<ReservaNacionalDTO>>(dtoList, HttpStatus.OK);
		}
		return new ResponseEntity<List<ReservaNacionalDTO>>(dtoList, HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/mostrarlistareservasporidcliente")
	public ResponseEntity<List<ReservaNacionalDTO>> getListaReservasPorIdCliente(@RequestParam Long id) {
		List<ReservaNacionalDTO> dtoList = nacionalServ.getListaReservasNacionalesIdCliente(id);

		if (dtoList != null) {
			return new ResponseEntity<List<ReservaNacionalDTO>>(dtoList, HttpStatus.OK);
		}
		return new ResponseEntity<List<ReservaNacionalDTO>>(dtoList, HttpStatus.NO_CONTENT);
	}
}
