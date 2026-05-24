package co.edu.unbosque.travelx.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import co.edu.unbosque.travelx.dto.VisaRequirementDTO;

/**
 * Servicio que consulta los requisitos de visa para un viajero según su país
 * de pasaporte, país de destino y fecha de viaje, mediante la API de RapidAPI.
 */
@Service
public class VisaRequirementService {

	@Value("${rapidapi.visa.host:visa-requirement.p.rapidapi.com}")
	private String visaHost;

	@Value("${rapidapi.visa.key}")
	private String visaKey;

	private final RapidApiClient rapidApiClient;

	public VisaRequirementService(RapidApiClient rapidApiClient) {
		this.rapidApiClient = rapidApiClient;
	}

	/**
	 * Consulta el requisito de visa para el país de pasaporte y destino indicados
	 * en una fecha específica, mapeando la respuesta al DTO correspondiente.
	 *
	 * @param pcc   código del país del pasaporte del viajero
	 * @param dcc   código del país de destino
	 * @param fecha fecha prevista del viaje
	 * @return {@link VisaRequirementDTO} con el resultado de la consulta y el estado del proveedor
	 */
	public VisaRequirementDTO checkVisaRequirement(String pcc, String dcc, LocalDate fecha) {
		String url = "https://" + visaHost + "/v2/visa/check/history/"
				+ pcc.toUpperCase() + "/"
				+ dcc.toUpperCase() + "/"
				+ fecha;

		String json = rapidApiClient.doGet(url, visaHost, visaKey);

		VisaRequirementDTO dto = new VisaRequirementDTO();
		dto.setPassportCountryCode(pcc.toUpperCase());
		dto.setDestinationCountryCode(dcc.toUpperCase());
		dto.setTravelDate(fecha.toString());
		dto.setProviderResponse(json);

		fillVisaStatus(dto, json);

		return dto;
	}

	/**
	 * Interpreta la respuesta JSON del proveedor y establece el requisito de visa,
	 * mensaje y estado en el DTO según el resultado obtenido.
	 *
	 * @param dto  objeto donde se almacena el resultado de la consulta
	 * @param json respuesta en formato JSON recibida del proveedor
	 */
	private void fillVisaStatus(VisaRequirementDTO dto, String json) {
		if (json == null || json.isBlank()) {
			dto.setVisaRequired(null);
			dto.setRequirement("UNKNOWN");
			dto.setMessage("No se recibio respuesta del proveedor de visa.");
			dto.setProviderSuccess(false);
			dto.setProviderResponse("");
			return;
		}

		try {
			JsonObject root = JsonParser.parseString(json).getAsJsonObject();

			if (root.has("statusCode")) {
				dto.setProviderStatusCode(root.get("statusCode").getAsInt());
				dto.setProviderSuccess(false);

				String error = readString(root, "error");
				String providerMessage = extractProviderMessage(error);

				dto.setProviderMessage(providerMessage);
				dto.setMessage(providerMessage);
				dto.setRequirement("UNKNOWN");
				dto.setVisaRequired(null);
				return;
			}

			dto.setProviderStatusCode(200);
			dto.setProviderSuccess(true);

			Boolean isDemo = extractIsDemo(root);
			String metaMessage = extractMetaMessage(root);

			String providerMessage = isDemo
					? "Respuesta demo del proveedor. " + defaultString(metaMessage, "")
					: "Consulta realizada exitosamente.";

			dto.setProviderMessage(providerMessage);

			Boolean visaRequired = extractVisaRequired(root);
			String requirement = extractRequirement(root);

			dto.setVisaRequired(visaRequired);
			dto.setRequirement(requirement);

			if (visaRequired == null) {
				dto.setMessage("No se pudo determinar si requiere visa.");
			} else if (visaRequired) {
				dto.setMessage("Si requiere visa.");
			} else {
				dto.setMessage("No requiere visa.");
			}

		} catch (Exception e) {
			dto.setVisaRequired(null);
			dto.setRequirement("UNKNOWN");
			dto.setMessage("No se pudo interpretar la respuesta del proveedor de visa.");
			dto.setProviderSuccess(false);
		}
	}

	/**
	 * Determina si se requiere visa a partir del texto del requisito extraído,
	 * evaluando palabras clave en el valor normalizado.
	 *
	 * @param root objeto JSON raíz de la respuesta del proveedor
	 * @return {@code true} si se requiere visa, {@code false} si no se requiere,
	 *         o {@code null} si no puede determinarse
	 */
	private Boolean extractVisaRequired(JsonObject root) {
		String requirement = extractRequirement(root);

		if (requirement == null) {
			return null;
		}

		String normalized = requirement.toLowerCase();

		if (normalized.contains("not required")
				|| normalized.contains("visa free")
				|| normalized.contains("no visa")) {
			return false;
		}

		if (normalized.contains("visa required")
				|| normalized.equals("required")
				|| normalized.contains("required")) {
			return true;
		}

		return null;
	}

	/**
	 * Extrae el texto del requisito de visa desde la respuesta JSON del proveedor,
	 * buscando primero en {@code data.current_rule} y luego en campos genéricos.
	 *
	 * @param root objeto JSON raíz de la respuesta del proveedor
	 * @return texto del requisito de visa, o {@code null} si no se encuentra
	 */
	private String extractRequirement(JsonObject root) {
		if (root.has("data") && root.get("data").isJsonObject()) {
			JsonObject data = root.getAsJsonObject("data");

			if (data.has("current_rule") && data.get("current_rule").isJsonObject()) {
				JsonObject currentRule = data.getAsJsonObject("current_rule");

				String displayLabel = readString(currentRule, "display_label");
				if (displayLabel != null && !displayLabel.isBlank()) {
					return displayLabel;
				}

				String name = readString(currentRule, "name");
				if (name != null && !name.isBlank()) {
					return name;
				}
			}
		}

		String[] fields = { "requirement", "visa", "result", "status", "message" };

		for (String field : fields) {
			String value = readString(root, field);

			if (value != null && !value.isBlank()) {
				return value;
			}
		}

		return null;
	}

	/**
	 * Verifica si la respuesta del proveedor corresponde a un modo de demostración.
	 *
	 * @param root objeto JSON raíz de la respuesta del proveedor
	 * @return {@code true} si la respuesta es demo, {@code false} en caso contrario
	 */
	private Boolean extractIsDemo(JsonObject root) {
		if (root.has("meta") && root.get("meta").isJsonObject()) {
			JsonObject meta = root.getAsJsonObject("meta");

			if (meta.has("is_demo") && !meta.get("is_demo").isJsonNull()) {
				return meta.get("is_demo").getAsBoolean();
			}
		}

		return false;
	}

	/**
	 * Extrae el mensaje del campo {@code meta} de la respuesta JSON del proveedor.
	 *
	 * @param root objeto JSON raíz de la respuesta del proveedor
	 * @return mensaje del campo meta, o {@code null} si no existe
	 */
	private String extractMetaMessage(JsonObject root) {
		if (root.has("meta") && root.get("meta").isJsonObject()) {
			JsonObject meta = root.getAsJsonObject("meta");
			return readString(meta, "message");
		}

		return null;
	}

	/**
	 * Extrae el mensaje de error desde una cadena JSON de error del proveedor.
	 * Si no puede interpretarse como JSON, retorna la cadena original.
	 *
	 * @param error cadena con el error retornado por el proveedor
	 * @return mensaje de error legible extraído del JSON, o la cadena original
	 */
	private String extractProviderMessage(String error) {
		if (error == null || error.isBlank()) {
			return "Error desconocido del proveedor de visa.";
		}

		try {
			JsonObject errorObject = JsonParser.parseString(error).getAsJsonObject();

			if (errorObject.has("message") && !errorObject.get("message").isJsonNull()) {
				return errorObject.get("message").getAsString();
			}
		} catch (Exception e) {
			return error;
		}

		return error;
	}

	/**
	 * Lee un campo de texto de un objeto JSON de forma segura,
	 * retornando null si el campo no existe o es nulo.
	 *
	 * @param object    objeto JSON del que se desea leer el campo
	 * @param fieldName nombre del campo a leer
	 * @return valor del campo como texto, o null si no existe
	 */
	private String readString(JsonObject object, String fieldName) {
		if (object.has(fieldName) && !object.get(fieldName).isJsonNull()) {
			return object.get(fieldName).getAsString();
		}

		return null;
	}

	/**
	 * Retorna el valor dado si no es nulo ni vacío, o el valor por defecto en caso contrario.
	 *
	 * @param value        valor a evaluar
	 * @param defaultValue valor por defecto a usar si el valor es nulo o vacío
	 * @return valor original o valor por defecto
	 */
	private String defaultString(String value, String defaultValue) {
		if (value == null || value.isBlank()) {
			return defaultValue;
		}

		return value;
	}
}