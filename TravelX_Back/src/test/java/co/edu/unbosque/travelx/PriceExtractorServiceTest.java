package co.edu.unbosque.travelx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import co.edu.unbosque.travelx.service.PriceExtractorService;

/**
 * Clase PriceExtractorServiceTest.
 */
public class PriceExtractorServiceTest {

	/**
	 * Atributo service priceExtractorService.
	 */
	private final PriceExtractorService priceExtractorService = new PriceExtractorService();

	/**
	 * Metodo comprobar extraer primer precio.
	 */
	@Test
	public void comprobarExtraerPrimerPrecio() {

		String json = "{\"hotel\":{\"displayPrice\":\"USD 125.50\"}}";

		Double resultado = priceExtractorService.extractFirstPrice(json);

		assertEquals(125.50, resultado);
	}

	/**
	 * Metodo comprobar extraer precio anidado.
	 */
	@Test
	public void comprobarExtraerPrecioAnidado() {

		String json = "{\"data\":[{\"route\":{\"totalPrice\":{\"amount\":300}}}]}";

		Double resultado = priceExtractorService.extractFirstPrice(json);

		assertEquals(300.0, resultado);
	}

	/**
	 * Metodo comprobar json invalido.
	 */
	@Test
	public void comprobarJsonInvalido() {

		Double resultado = priceExtractorService.extractFirstPrice("{json invalido}");

		assertNull(resultado);
	}

	/**
	 * Metodo comprobar json vacio.
	 */
	@Test
	public void comprobarJsonVacio() {

		Double resultado = priceExtractorService.extractFirstPrice("");

		assertNull(resultado);
	}

	/**
	 * Metodo comprobar extraer precio kiwi.
	 */
	@Test
	public void comprobarExtraerPrecioKiwi() {

		String json = "{\"topResults\":{\"best\":{\"price\":{\"amount\":450.75}}}}";

		Double resultado = priceExtractorService.extractKiwiPrice(json);

		assertEquals(450.75, resultado);
	}

	/**
	 * Metodo comprobar extraer precio kiwi recursivo.
	 */
	@Test
	public void comprobarExtraerPrecioKiwiRecursivo() {

		String json = "{\"results\":[{\"item\":{\"price\":{\"amount\":\"USD 99\"}}}]}";

		Double resultado = priceExtractorService.extractKiwiPrice(json);

		assertEquals(99.0, resultado);
	}
}
