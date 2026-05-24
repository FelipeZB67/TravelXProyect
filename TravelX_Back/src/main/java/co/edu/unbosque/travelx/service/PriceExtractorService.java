package co.edu.unbosque.travelx.service;

import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Servicio que extrae precios desde respuestas JSON de proveedores externos,
 * realizando búsquedas recursivas sobre la estructura del JSON para localizar
 * el primer valor de precio disponible.
 */
@Service
public class PriceExtractorService {

	/**
	 * Extrae el primer precio encontrado en una respuesta JSON genérica,
	 * buscando recursivamente claves relacionadas con precios.
	 *
	 * @param json cadena JSON de la que se desea extraer el precio
	 * @return primer precio encontrado como {@link Double}, o {@code null} si no se encuentra
	 */
	public Double extractFirstPrice(String json) {
		if (json == null || json.isBlank()) {
			return null;
		}

		try {
			JsonElement root = JsonParser.parseString(json);
			return searchPrice(root);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Extrae el precio desde una respuesta JSON con estructura específica de Kiwi,
	 * priorizando el campo {@code topResults.best.price.amount} antes de realizar
	 * una búsqueda recursiva.
	 *
	 * @param json cadena JSON de respuesta de Kiwi
	 * @return precio extraído como {@link Double}, o {@code null} si no se encuentra
	 */
	public Double extractKiwiPrice(String json) {
		if (json == null || json.isBlank()) {
			return null;
		}

		try {
			JsonObject root = JsonParser.parseString(json).getAsJsonObject();

			if (root.has("topResults") && root.get("topResults").isJsonObject()) {
				JsonObject topResults = root.getAsJsonObject("topResults");

				if (topResults.has("best") && topResults.get("best").isJsonObject()) {
					JsonObject best = topResults.getAsJsonObject("best");

					if (best.has("price") && best.get("price").isJsonObject()) {
						JsonObject price = best.getAsJsonObject("price");

						if (price.has("amount") && !price.get("amount").isJsonNull()) {
							return parsePrice(price.get("amount"));
						}
					}
				}
			}

			return searchKiwiPrice(root);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Busca recursivamente el primer valor de precio en un elemento JSON,
	 * evaluando claves relacionadas con precios en objetos y recorriendo arrays.
	 *
	 * @param element elemento JSON donde se realiza la búsqueda
	 * @return primer precio encontrado como {@link Double}, o {@code null} si no se encuentra
	 */
	private Double searchPrice(JsonElement element) {
		if (element == null || element.isJsonNull()) {
			return null;
		}

		if (element.isJsonObject()) {
			JsonObject object = element.getAsJsonObject();

			for (String key : object.keySet()) {
				JsonElement value = object.get(key);
				String normalizedKey = key.toLowerCase();

				if (isPriceKey(normalizedKey)) {
					Double price = parsePrice(value);

					if (price != null) {
						return price;
					}
				}

				Double nestedPrice = searchPrice(value);

				if (nestedPrice != null) {
					return nestedPrice;
				}
			}
		}

		if (element.isJsonArray()) {
			JsonArray array = element.getAsJsonArray();

			for (JsonElement item : array) {
				Double nestedPrice = searchPrice(item);

				if (nestedPrice != null) {
					return nestedPrice;
				}
			}
		}

		return null;
	}

	/**
	 * Busca recursivamente un precio en la estructura JSON de Kiwi,
	 * priorizando objetos con la forma {@code price.amount}.
	 *
	 * @param element elemento JSON donde se realiza la búsqueda
	 * @return precio encontrado como {@link Double}, o {@code null} si no se encuentra
	 */
	private Double searchKiwiPrice(JsonElement element) {
		if (element == null || element.isJsonNull()) {
			return null;
		}

		if (element.isJsonObject()) {
			JsonObject object = element.getAsJsonObject();

			if (object.has("price") && object.get("price").isJsonObject()) {
				JsonObject priceObject = object.getAsJsonObject("price");

				if (priceObject.has("amount") && !priceObject.get("amount").isJsonNull()) {
					return parsePrice(priceObject.get("amount"));
				}
			}

			for (String key : object.keySet()) {
				Double price = searchKiwiPrice(object.get(key));

				if (price != null) {
					return price;
				}
			}
		}

		if (element.isJsonArray()) {
			for (JsonElement item : element.getAsJsonArray()) {
				Double price = searchKiwiPrice(item);

				if (price != null) {
					return price;
				}
			}
		}

		return null;
	}

	/**
	 * Determina si una clave JSON está relacionada con un valor de precio,
	 * verificando si contiene términos comunes de precios.
	 *
	 * @param key nombre de la clave JSON en minúsculas
	 * @return {@code true} si la clave está relacionada con un precio
	 */
	private boolean isPriceKey(String key) {
		return key.contains("price")
				|| key.contains("amount")
				|| key.contains("total")
				|| key.contains("fare")
				|| key.contains("cost")
				|| key.contains("rate")
				|| key.contains("value")
				|| key.contains("rawprice")
				|| key.contains("displayprice")
				|| key.contains("formattedprice")
				|| key.contains("totalprice")
				|| key.contains("basefare");
	}

	/**
	 * Convierte un elemento JSON a un valor numérico de tipo {@link Double},
	 * soportando primitivos numéricos, cadenas de texto y objetos con campos de precio.
	 *
	 * @param value elemento JSON que representa un precio
	 * @return valor numérico del precio, o {@code null} si no puede convertirse
	 */
	private Double parsePrice(JsonElement value) {
		try {
			if (value == null || value.isJsonNull()) {
				return null;
			}

			if (value.isJsonPrimitive() && value.getAsJsonPrimitive().isNumber()) {
				return value.getAsDouble();
			}

			if (value.isJsonPrimitive() && value.getAsJsonPrimitive().isString()) {
				String text = value.getAsString().replace(",", ".").replaceAll("[^0-9.]", "");

				if (!text.isBlank()) {
					return Double.parseDouble(text);
				}
			}

			if (value.isJsonObject()) {
				JsonObject object = value.getAsJsonObject();

				String[] priorityFields = { "amount", "value", "price", "raw", "rawPrice", "total", "totalPrice" };

				for (String field : priorityFields) {
					if (object.has(field) && !object.get(field).isJsonNull()) {
						Double price = parsePrice(object.get(field));

						if (price != null) {
							return price;
						}
					}
				}
			}
		} catch (Exception e) {
			return null;
		}

		return null;
	}
}