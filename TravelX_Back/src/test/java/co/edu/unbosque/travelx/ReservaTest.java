package co.edu.unbosque.travelx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.unbosque.travelx.dto.TravelOptionDTO;
import co.edu.unbosque.travelx.entity.Reserva;
import co.edu.unbosque.travelx.repository.ReservaRepository;
import co.edu.unbosque.travelx.service.ReservaService;

/**
 * Clase ReservaTest.
 */
@ExtendWith(MockitoExtension.class)
public class ReservaTest {

	/**
	 * Atributo repositorio reservaRepository.
	 */
	@Mock
	private ReservaRepository reservaRepository;

	/**
	 * Atributo service reservaService.
	 */
	@InjectMocks
	private ReservaService reservaService;

	/**
	 * Metodo comprobar crear.
	 */
	@Test
	public void comprobarCrear() {

		TravelOptionDTO option = crearTravelOption();

		when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Reserva resultado = reservaService.crearReserva("usuario@gmail.com", option);

		assertEquals("usuario@gmail.com", resultado.getUsername());
		assertEquals(option.getProvider(), resultado.getProvider());
		assertEquals(option.getTitle(), resultado.getTitle());
		assertEquals(option.getPrice(), resultado.getPrice());
		assertEquals(option.getProviderResponse(), resultado.getProviderResponse());
		verify(reservaRepository, times(1)).save(any(Reserva.class));
	}

	/**
	 * Metodo comprobar mis reservas.
	 */
	@Test
	public void comprobarMisReservas() {

		Reserva reserva = new Reserva();
		reserva.setUsername("usuario@gmail.com");

		when(reservaRepository.findByUsernameOrderByFechaCreacionDesc("usuario@gmail.com"))
				.thenReturn(Arrays.asList(reserva));

		List<Reserva> resultado = reservaService.misReservas("usuario@gmail.com");

		assertEquals(1, resultado.size());
		assertEquals("usuario@gmail.com", resultado.get(0).getUsername());
	}

	/**
	 * Metodo comprobar todas las reservas.
	 */
	@Test
	public void comprobarTodasReservas() {

		Reserva reserva = new Reserva();

		when(reservaRepository.findAllByOrderByFechaCreacionDesc()).thenReturn(Arrays.asList(reserva));

		List<Reserva> resultado = reservaService.todasReservas();

		assertEquals(1, resultado.size());
	}

	/**
	 * Metodo comprobar crear reserva administrador.
	 */
	@Test
	public void comprobarCrearReservaAdmin() {

		Reserva reserva = new Reserva();
		reserva.setUsername("admin@unbosque.edu.co");
		reserva.setPrice(150.0);

		when(reservaRepository.save(reserva)).thenReturn(reserva);

		Reserva resultado = reservaService.crearReservaAdmin(reserva);

		assertEquals("TRAVELX_ADMIN", resultado.getProvider());
		assertEquals("USD", resultado.getCurrency());
		assertTrue(resultado.getAvailable());
		assertTrue(resultado.getProviderSuccess());
		assertEquals(200, resultado.getProviderStatusCode());
		assertEquals("150.0 USD", resultado.getPriceText());
		verify(reservaRepository, times(1)).save(reserva);
	}

	/**
	 * Metodo comprobar actualizar reserva administrador.
	 */
	@Test
	public void comprobarActualizarReservaAdmin() {

		Reserva actual = new Reserva();
		actual.setId(1L);

		Reserva datos = new Reserva();
		datos.setUsername("usuario@gmail.com");
		datos.setProvider("");
		datos.setCurrency("");
		datos.setPrice(250.0);
		datos.setTitle("Vuelo Bogota Medellin");

		when(reservaRepository.findById(1L)).thenReturn(Optional.of(actual));
		when(reservaRepository.save(actual)).thenReturn(actual);

		Reserva resultado = reservaService.actualizarReservaAdmin(1L, datos);

		assertEquals("usuario@gmail.com", resultado.getUsername());
		assertEquals("TRAVELX_ADMIN", resultado.getProvider());
		assertEquals("USD", resultado.getCurrency());
		assertEquals("250.0 USD", resultado.getPriceText());
		assertEquals("Vuelo Bogota Medellin", resultado.getTitle());
		verify(reservaRepository, times(1)).save(actual);
	}

	/**
	 * Metodo comprobar eliminar reserva administrador.
	 */
	@Test
	public void comprobarEliminarReservaAdmin() {

		reservaService.eliminarReservaAdmin(1L);

		verify(reservaRepository, times(1)).deleteById(1L);
	}

	/**
	 * Metodo comprobar buscar propia.
	 */
	@Test
	public void comprobarBuscarPropia() {

		Reserva reserva = new Reserva();
		reserva.setId(1L);
		reserva.setUsername("usuario@gmail.com");

		when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

		Reserva resultado = reservaService.buscarPropia(1L, "usuario@gmail.com");

		assertEquals(reserva, resultado);
	}

	/**
	 * Metodo comprobar buscar propia con usuario incorrecto.
	 */
	@Test
	public void comprobarBuscarPropiaUsuarioIncorrecto() {

		Reserva reserva = new Reserva();
		reserva.setId(1L);
		reserva.setUsername("usuario@gmail.com");

		when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

		assertThrows(RuntimeException.class, () -> reservaService.buscarPropia(1L, "otro@gmail.com"));
	}

	/**
	 * Crea una opcion de viaje para las pruebas.
	 */
	private TravelOptionDTO crearTravelOption() {

		TravelOptionDTO option = new TravelOptionDTO();
		option.setProvider("GOOGLE_FLIGHTS");
		option.setType("FLIGHT");
		option.setTitle("Vuelo Bogota Medellin");
		option.setDescription("Vuelo directo");
		option.setOriginCity("Bogota");
		option.setOriginCountry("Colombia");
		option.setDestinationCity("Medellin");
		option.setDestinationCountry("Colombia");
		option.setDepartureDate("2026-06-01");
		option.setReturnDate("2026-06-10");
		option.setCurrency("USD");
		option.setPrice(120.0);
		option.setPriceText("120 USD");
		option.setAdults(2);
		option.setChildren(1);
		option.setPets(0);
		option.setTravelClass("ECONOMY");
		option.setHasPool(false);
		option.setHasJacuzzi(false);
		option.setPetFriendly(false);
		option.setAvailable(true);
		option.setBookingUrl("https://travelx.com/reserva");
		option.setProviderStatusCode(200);
		option.setProviderSuccess(true);
		option.setProviderMessage("OK");
		option.setProviderResponse("{\"success\":true}");
		return option;
	}
}
