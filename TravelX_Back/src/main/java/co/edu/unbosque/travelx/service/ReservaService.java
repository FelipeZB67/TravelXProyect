package co.edu.unbosque.travelx.service;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.unbosque.travelx.dto.TravelOptionDTO;
import co.edu.unbosque.travelx.entity.Reserva;
import co.edu.unbosque.travelx.repository.ReservaRepository;

/**
 * Servicio que gestiona las operaciones sobre reservas de viaje,
 * incluyendo creación, consulta, actualización y eliminación,
 * tanto para usuarios como para administradores.
 */
@Service
public class ReservaService {

	private final ReservaRepository reservaRepository;

	public ReservaService(ReservaRepository reservaRepository) {
		this.reservaRepository = reservaRepository;
	}

	/**
	 * Crea y persiste una reserva para el usuario autenticado a partir
	 * de una opción de viaje seleccionada.
	 *
	 * @param username nombre de usuario al que se asocia la reserva
	 * @param option   objeto con los datos de la opción de viaje a reservar
	 * @return {@link Reserva} persistida con todos los datos de la opción de viaje
	 */
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

	/**
	 * Retorna las reservas del usuario indicado ordenadas de más reciente a más antigua.
	 *
	 * @param username nombre de usuario cuyas reservas se desean consultar
	 * @return lista de {@link Reserva} del usuario ordenadas por fecha de creación descendente
	 */
	public List<Reserva> misReservas(String username) {
		return reservaRepository.findByUsernameOrderByFechaCreacionDesc(username);
	}

	/**
	 * Retorna todas las reservas del sistema ordenadas de más reciente a más antigua.
	 *
	 * @return lista de todas las {@link Reserva} ordenadas por fecha de creación descendente
	 */
	public List<Reserva> todasReservas() {
		return reservaRepository.findAllByOrderByFechaCreacionDesc();
	}
	
	/**
	 * Crea y persiste una reserva desde el panel de administración,
	 * aplicando valores por defecto a los campos no especificados.
	 *
	 * @param reserva objeto con los datos de la reserva a crear
	 * @return {@link Reserva} persistida con los valores completados
	 */
	public Reserva crearReservaAdmin(Reserva reserva) {
		prepararReservaAdmin(reserva);
		return reservaRepository.save(reserva);
	}

	/**
	 * Actualiza todos los campos de una reserva existente desde el panel
	 * de administración, aplicando valores por defecto a los campos vacíos.
	 *
	 * @param id    identificador de la reserva a actualizar
	 * @param datos objeto con los nuevos datos de la reserva
	 * @return {@link Reserva} actualizada y persistida
	 * @throws java.util.NoSuchElementException si no existe una reserva con el ID indicado
	 */
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

	/**
	 * Elimina una reserva del sistema según su ID desde el panel de administración.
	 *
	 * @param id identificador de la reserva a eliminar
	 */
	public void eliminarReservaAdmin(Long id) {
		reservaRepository.deleteById(id);
	}

	/**
	 * Aplica valores por defecto a los campos vacíos o nulos de una reserva
	 * creada o actualizada desde el panel de administración.
	 *
	 * @param reserva objeto al que se le aplican los valores por defecto
	 */
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

	/**
	 * Busca una reserva por su ID verificando que pertenezca al usuario indicado.
	 *
	 * @param id       identificador de la reserva a buscar
	 * @param username nombre de usuario que intenta acceder a la reserva
	 * @return {@link Reserva} encontrada si pertenece al usuario
	 * @throws RuntimeException si la reserva no pertenece al usuario indicado
	 * @throws java.util.NoSuchElementException si no existe una reserva con el ID indicado
	 */
	public Reserva buscarPropia(Long id, String username) {
		Reserva reserva = reservaRepository.findById(id).orElseThrow();

		if (!reserva.getUsername().equals(username)) {
			throw new RuntimeException("No puede ver esta reserva.");
		}

		return reserva;
	}
}