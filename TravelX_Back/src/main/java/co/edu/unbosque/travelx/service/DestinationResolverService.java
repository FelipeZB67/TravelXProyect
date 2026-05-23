package co.edu.unbosque.travelx.service;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import co.edu.unbosque.travelx.dto.DestinationCodeDTO;

@Service
public class DestinationResolverService {

	private final Map<String, DestinationCodeDTO> destinations = new HashMap<>();

	public DestinationResolverService() {
		destinations.put(key("Los Angeles", "Estados Unidos"),
				new DestinationCodeDTO("Los Angeles", "Estados Unidos", "LAX", "ChIJE9on3F3HwoAR9AhGJW_fL-I",
						"City:los-angeles_ca_us"));

		destinations.put(key("Los Angeles", "United States"),
				new DestinationCodeDTO("Los Angeles", "United States", "LAX", "ChIJE9on3F3HwoAR9AhGJW_fL-I",
						"City:los-angeles_ca_us"));

		destinations.put(key("New York", "Estados Unidos"),
				new DestinationCodeDTO("New York", "Estados Unidos", "JFK", "ChIJOwg_06VPwokRYv534QaPC8g",
						"City:new-york_ny_us"));

		destinations.put(key("New York", "United States"),
				new DestinationCodeDTO("New York", "United States", "JFK", "ChIJOwg_06VPwokRYv534QaPC8g",
						"City:new-york_ny_us"));

		destinations.put(key("Chicago", "Estados Unidos"),
				new DestinationCodeDTO("Chicago", "Estados Unidos", "ORD", "ChIJ7cv00DwsDogRAMDACa2m4K8",
						"City:chicago_il_us"));

		destinations.put(key("Chicago", "United States"),
				new DestinationCodeDTO("Chicago", "United States", "ORD", "ChIJ7cv00DwsDogRAMDACa2m4K8",
						"City:chicago_il_us"));

		destinations.put(key("Bogota", "Colombia"),
				new DestinationCodeDTO("Bogota", "Colombia", "BOG", "ChIJKcumLf2bP44RFDmjIFVjnSM",
						"City:bogota_co"));

		destinations.put(key("Medellin", "Colombia"),
				new DestinationCodeDTO("Medellin", "Colombia", "MDE", "ChIJBa0PuN8oRI4RVju1x_x8E0I",
						"City:medellin_co"));
	}

	public DestinationCodeDTO resolve(String city, String country) {
		return destinations.get(key(city, country));
	}

	private String key(String city, String country) {
		return normalize(city) + "|" + normalize(country);
	}

	private String normalize(String value) {
		if (value == null) {
			return "";
		}

		String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
		normalized = normalized.replaceAll("\\p{M}", "");
		return normalized.trim().toLowerCase();
	}
}