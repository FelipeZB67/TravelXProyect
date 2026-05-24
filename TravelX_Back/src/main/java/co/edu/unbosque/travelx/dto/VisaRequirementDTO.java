package co.edu.unbosque.travelx.dto;

/**
 * Objeto de transferencia de datos que representa el resultado de una consulta
 * de requisito de visa, indicando si se requiere visa según el país de pasaporte,
 * el país de destino y la fecha de viaje, junto con la respuesta del proveedor externo.
 */
public class VisaRequirementDTO {

	private String passportCountryCode;
	private String destinationCountryCode;
	private String travelDate;

	private Boolean visaRequired;
	private String requirement;
	private String message;

	private Integer providerStatusCode;
	private Boolean providerSuccess;
	private String providerMessage;
	
	private String providerResponse;

	public VisaRequirementDTO() {
	}

	public String getPassportCountryCode() {
		return passportCountryCode;
	}

	public void setPassportCountryCode(String passportCountryCode) {
		this.passportCountryCode = passportCountryCode;
	}

	public String getDestinationCountryCode() {
		return destinationCountryCode;
	}

	public void setDestinationCountryCode(String destinationCountryCode) {
		this.destinationCountryCode = destinationCountryCode;
	}

	public String getTravelDate() {
		return travelDate;
	}

	public void setTravelDate(String travelDate) {
		this.travelDate = travelDate;
	}

	public Boolean getVisaRequired() {
		return visaRequired;
	}

	public void setVisaRequired(Boolean visaRequired) {
		this.visaRequired = visaRequired;
	}

	public String getRequirement() {
		return requirement;
	}

	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getProviderStatusCode() {
		return providerStatusCode;
	}

	public void setProviderStatusCode(Integer providerStatusCode) {
		this.providerStatusCode = providerStatusCode;
	}

	public Boolean getProviderSuccess() {
		return providerSuccess;
	}

	public void setProviderSuccess(Boolean providerSuccess) {
		this.providerSuccess = providerSuccess;
	}

	public String getProviderMessage() {
		return providerMessage;
	}

	public void setProviderMessage(String providerMessage) {
		this.providerMessage = providerMessage;
	}

	public String getProviderResponse() {
		return providerResponse;
	}

	public void setProviderResponse(String providerResponse) {
		this.providerResponse = providerResponse;
	}

	
}