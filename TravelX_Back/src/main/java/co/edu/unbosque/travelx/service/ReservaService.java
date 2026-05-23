package co.edu.unbosque.travelx.service;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.unbosque.travelx.dto.TravelOptionDTO;
import co.edu.unbosque.travelx.entity.Reserva;
import co.edu.unbosque.travelx.repository.ReservaRepository;

@Service
public class ReservaService {

	private final ReservaRepository reservaRepository;

	public ReservaService(ReservaRepository reservaRepository) {
		this.reservaRepository = reservaRepository;
	}

	public Reserva crearReserva(String username, TravelOptionDTO option) {
		Reserva reserva = new Reserva();

		reserva.setUsername(username);
		reserva.setProvider(option.getProvider());
		reserva.setType(option.getType());
		reserva.setTitle(option.getTitle());
		reserva.setDescription(option.getDescription());
		reserva.setOriginCity(option.getOriginCity());
		reserva.setOriginCountry(option.getOriginCountry());
		reserva.setDestinationCity(option.getDestinationCity());
		reserva.setDestinationCountry(option.getDestinationCountry());
		reserva.setDepartureDate(option.getDepartureDate());
		reserva.setReturnDate(option.getReturnDate());
		reserva.setCurrency(option.getCurrency());
		reserva.setPrice(option.getPrice());
		reserva.setPriceText(option.getPriceText());
		reserva.setAdults(option.getAdults());
		reserva.setChildren(option.getChildren());
		reserva.setPets(option.getPets());
		reserva.setTravelClass(option.getTravelClass());
		reserva.setHasPool(option.getHasPool());
		reserva.setHasJacuzzi(option.getHasJacuzzi());
		reserva.setPetFriendly(option.getPetFriendly());
		reserva.setAvailable(option.getAvailable());
		reserva.setBookingUrl(option.getBookingUrl());
		reserva.setProviderStatusCode(option.getProviderStatusCode());
		reserva.setProviderSuccess(option.getProviderSuccess());
		reserva.setProviderMessage(option.getProviderMessage());
		reserva.setProviderResponse(option.getProviderResponse());

		return reservaRepository.save(reserva);
	}

	public List<Reserva> misReservas(String username) {
		return reservaRepository.findByUsernameOrderByFechaCreacionDesc(username);
	}

	public List<Reserva> todasReservas() {
		return reservaRepository.findAllByOrderByFechaCreacionDesc();
	}
	
	public Reserva crearReservaAdmin(Reserva reserva) {
		prepararReservaAdmin(reserva);
		return reservaRepository.save(reserva);
	}

	public Reserva actualizarReservaAdmin(Long id, Reserva datos) {
		Reserva reserva = reservaRepository.findById(id).orElseThrow();

		reserva.setUsername(datos.getUsername());
		reserva.setProvider(datos.getProvider());
		reserva.setType(datos.getType());
		reserva.setTitle(datos.getTitle());
		reserva.setDescription(datos.getDescription());
		reserva.setOriginCity(datos.getOriginCity());
		reserva.setOriginCountry(datos.getOriginCountry());
		reserva.setDestinationCity(datos.getDestinationCity());
		reserva.setDestinationCountry(datos.getDestinationCountry());
		reserva.setDepartureDate(datos.getDepartureDate());
		reserva.setReturnDate(datos.getReturnDate());
		reserva.setCurrency(datos.getCurrency());
		reserva.setPrice(datos.getPrice());
		reserva.setPriceText(datos.getPriceText());
		reserva.setAdults(datos.getAdults());
		reserva.setChildren(datos.getChildren());
		reserva.setPets(datos.getPets());
		reserva.setTravelClass(datos.getTravelClass());
		reserva.setHasPool(datos.getHasPool());
		reserva.setHasJacuzzi(datos.getHasJacuzzi());
		reserva.setPetFriendly(datos.getPetFriendly());
		reserva.setAvailable(datos.getAvailable());
		reserva.setBookingUrl(datos.getBookingUrl());
		reserva.setProviderStatusCode(datos.getProviderStatusCode());
		reserva.setProviderSuccess(datos.getProviderSuccess());
		reserva.setProviderMessage(datos.getProviderMessage());
		reserva.setProviderResponse(datos.getProviderResponse());

		prepararReservaAdmin(reserva);
		return reservaRepository.save(reserva);
	}

	public void eliminarReservaAdmin(Long id) {
		reservaRepository.deleteById(id);
	}

	private void prepararReservaAdmin(Reserva reserva) {
		if (reserva.getProvider() == null || reserva.getProvider().isBlank()) {
			reserva.setProvider("TRAVELX_ADMIN");
		}

		if (reserva.getCurrency() == null || reserva.getCurrency().isBlank()) {
			reserva.setCurrency("USD");
		}

		if (reserva.getAvailable() == null) {
			reserva.setAvailable(true);
		}

		if (reserva.getProviderSuccess() == null) {
			reserva.setProviderSuccess(true);
		}

		if (reserva.getProviderStatusCode() == null) {
			reserva.setProviderStatusCode(200);
		}

		if ((reserva.getPriceText() == null || reserva.getPriceText().isBlank()) && reserva.getPrice() != null) {
			reserva.setPriceText(reserva.getPrice() + " " + reserva.getCurrency());
		}
	}

	public Reserva buscarPropia(Long id, String username) {
		Reserva reserva = reservaRepository.findById(id).orElseThrow();

		if (!reserva.getUsername().equals(username)) {
			throw new RuntimeException("No puede ver esta reserva.");
		}

		return reserva;
	}
}