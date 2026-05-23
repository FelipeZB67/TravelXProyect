package co.edu.unbosque.travelx.dto;

public class GoogleFlightsSearchRequestDTO {

	private String departureId;
	private String arrivalId;
	private String outboundDate;
	private String returnDate;
	private String travelClass;
	private String adults;
	private String children;
	private String infantOnLap;
	private String infantInSeat;
	private String showHidden;
	private String currency;
	private String languageCode;
	private String countryCode;
	private String searchType;

	public GoogleFlightsSearchRequestDTO() {
	}

	public String getDepartureId() {
		return departureId;
	}

	public void setDepartureId(String departureId) {
		this.departureId = departureId;
	}

	public String getArrivalId() {
		return arrivalId;
	}

	public void setArrivalId(String arrivalId) {
		this.arrivalId = arrivalId;
	}

	public String getOutboundDate() {
		return outboundDate;
	}

	public void setOutboundDate(String outboundDate) {
		this.outboundDate = outboundDate;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public String getTravelClass() {
		return travelClass;
	}

	public void setTravelClass(String travelClass) {
		this.travelClass = travelClass;
	}

	public String getAdults() {
		return adults;
	}

	public void setAdults(String adults) {
		this.adults = adults;
	}

	public String getChildren() {
		return children;
	}

	public void setChildren(String children) {
		this.children = children;
	}

	public String getInfantOnLap() {
		return infantOnLap;
	}

	public void setInfantOnLap(String infantOnLap) {
		this.infantOnLap = infantOnLap;
	}

	public String getInfantInSeat() {
		return infantInSeat;
	}

	public void setInfantInSeat(String infantInSeat) {
		this.infantInSeat = infantInSeat;
	}

	public String getShowHidden() {
		return showHidden;
	}

	public void setShowHidden(String showHidden) {
		this.showHidden = showHidden;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
}