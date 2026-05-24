package co.edu.unbosque.travelx.dto;

import java.util.List;

/**
 * Objeto de transferencia de datos que representa la respuesta de una búsqueda
 * unificada de viajes, incluyendo las ubicaciones resueltas de origen y destino,
 * la solicitud original y la lista de opciones de viaje disponibles.
 */
public class TravelSearchResponseDTO {

	private Boolean success;
	private String message;
	private TravelSearchRequestDTO request;
	private NominatimResolvedLocationDTO resolvedOrigin;
	private NominatimResolvedLocationDTO resolvedDestination;
	private List<TravelOptionDTO> options;

	public TravelSearchResponseDTO() {
	}

	public NominatimResolvedLocationDTO getResolvedOrigin() {
		return resolvedOrigin;
	}

	public void setResolvedOrigin(NominatimResolvedLocationDTO resolvedOrigin) {
		this.resolvedOrigin = resolvedOrigin;
	}

	public NominatimResolvedLocationDTO getResolvedDestination() {
		return resolvedDestination;
	}

	public void setResolvedDestination(NominatimResolvedLocationDTO resolvedDestination) {
		this.resolvedDestination = resolvedDestination;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public TravelSearchRequestDTO getRequest() {
		return request;
	}

	public void setRequest(TravelSearchRequestDTO request) {
		this.request = request;
	}

	public List<TravelOptionDTO> getOptions() {
		return options;
	}

	public void setOptions(List<TravelOptionDTO> options) {
		this.options = options;
	}
	
}