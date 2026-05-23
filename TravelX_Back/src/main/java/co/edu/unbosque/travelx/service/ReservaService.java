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

	public Reserva buscarPropia(Long id, String username) {
		Reserva reserva = reservaRepository.findById(id).orElseThrow();

		if (!reserva.getUsername().equals(username)) {
			throw new RuntimeException("No puede ver esta reserva.");
		}

		return reserva;
	}
}