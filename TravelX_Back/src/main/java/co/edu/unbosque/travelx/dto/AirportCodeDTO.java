package co.edu.unbosque.travelx.dto;

public class AirportCodeDTO {

	private String query;
	private String countryCode;
	private String iataCode;
	private String name;
	private Boolean found;
	private String message;
	private String providerResponse;

	public AirportCodeDTO() {
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getIataCode() {
		return iataCode;
	}

	public void setIataCode(String iataCode) {
		this.iataCode = iataCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getFound() {
		return found;
	}

	public void setFound(Boolean found) {
		this.found = found;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getProviderResponse() {
		return providerResponse;
	}

	public void setProviderResponse(String providerResponse) {
		this.providerResponse = providerResponse;
	}

	
}