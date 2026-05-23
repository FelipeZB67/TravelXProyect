package co.edu.unbosque.travelx.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import co.edu.unbosque.travelx.dto.VisaRequirementDTO;

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

	private Boolean extractIsDemo(JsonObject root) {
		if (root.has("meta") && root.get("meta").isJsonObject()) {
			JsonObject meta = root.getAsJsonObject("meta");

			if (meta.has("is_demo") && !meta.get("is_demo").isJsonNull()) {
				return meta.get("is_demo").getAsBoolean();
			}
		}

		return false;
	}

	private String extractMetaMessage(JsonObject root) {
		if (root.has("meta") && root.get("meta").isJsonObject()) {
			JsonObject meta = root.getAsJsonObject("meta");
			return readString(meta, "message");
		}

		return null;
	}

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

	private String readString(JsonObject object, String fieldName) {
		if (object.has(fieldName) && !object.get(fieldName).isJsonNull()) {
			return object.get(fieldName).getAsString();
		}

		return null;
	}

	private String defaultString(String value, String defaultValue) {
		if (value == null || value.isBlank()) {
			return defaultValue;
		}

		return value;
	}
}