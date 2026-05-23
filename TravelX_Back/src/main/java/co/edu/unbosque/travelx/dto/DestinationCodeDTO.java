package co.edu.unbosque.travelx.dto;

public class DestinationCodeDTO {

	private String cityName;
	private String countryCode;
	private String iataCode;
	private String googlePlaceId;
	private String kiwiCode;

	public DestinationCodeDTO() {
	}

	public DestinationCodeDTO(String cityName, String countryCode, String iataCode, String googlePlaceId,
			String kiwiCode) {
		this.cityName = cityName;
		this.countryCode = countryCode;
		this.iataCode = iataCode;
		this.googlePlaceId = googlePlaceId;
		this.kiwiCode = kiwiCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
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

	public String getGooglePlaceId() {
		return googlePlaceId;
	}

	public void setGooglePlaceId(String googlePlaceId) {
		this.googlePlaceId = googlePlaceId;
	}

	public String getKiwiCode() {
		return kiwiCode;
	}

	public void setKiwiCode(String kiwiCode) {
		this.kiwiCode = kiwiCode;
	}

	
}