package co.edu.unbosque.travelx.dto;

/**
 * Objeto de transferencia de datos que representa la respuesta de una búsqueda
 * de alojamientos tipo Airbnb, incluyendo los filtros aplicados y el resultado
 * retornado por el proveedor externo.
 */
public class AirbnbSearchDTO {

	private String placeId;
	private String nextPageCursor;
	private String checkin;
	private String checkout;
	private Integer adults;
	private Integer children;
	private Integer infants;
	private Integer pets;
	private Integer priceMin;
	private Integer priceMax;
	private Integer minBedrooms;
	private Integer minBeds;
	private String amenities;
	private Boolean guestFavorite;
	private Boolean ib;
	private String flexibleDateSearchFilterType;
	private String currency;

	private Integer statusCode;
	private Boolean success;
	private String message;
	private String providerResponse;

	public AirbnbSearchDTO() {
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getNextPageCursor() {
		return nextPageCursor;
	}

	public void setNextPageCursor(String nextPageCursor) {
		this.nextPageCursor = nextPageCursor;
	}

	public String getCheckin() {
		return checkin;
	}

	public void setCheckin(String checkin) {
		this.checkin = checkin;
	}

	public String getCheckout() {
		return checkout;
	}

	public void setCheckout(String checkout) {
		this.checkout = checkout;
	}

	public Integer getAdults() {
		return adults;
	}

	public void setAdults(Integer adults) {
		this.adults = adults;
	}

	public Integer getChildren() {
		return children;
	}

	public void setChildren(Integer children) {
		this.children = children;
	}

	public Integer getInfants() {
		return infants;
	}

	public void setInfants(Integer infants) {
		this.infants = infants;
	}

	public Integer getPets() {
		return pets;
	}

	public void setPets(Integer pets) {
		this.pets = pets;
	}

	public Integer getPriceMin() {
		return priceMin;
	}

	public void setPriceMin(Integer priceMin) {
		this.priceMin = priceMin;
	}

	public Integer getPriceMax() {
		return priceMax;
	}

	public void setPriceMax(Integer priceMax) {
		this.priceMax = priceMax;
	}

	public Integer getMinBedrooms() {
		return minBedrooms;
	}

	public void setMinBedrooms(Integer minBedrooms) {
		this.minBedrooms = minBedrooms;
	}

	public Integer getMinBeds() {
		return minBeds;
	}

	public void setMinBeds(Integer minBeds) {
		this.minBeds = minBeds;
	}

	public String getAmenities() {
		return amenities;
	}

	public void setAmenities(String amenities) {
		this.amenities = amenities;
	}

	public Boolean getGuestFavorite() {
		return guestFavorite;
	}

	public void setGuestFavorite(Boolean guestFavorite) {
		this.guestFavorite = guestFavorite;
	}

	public Boolean getIb() {
		return ib;
	}

	public void setIb(Boolean ib) {
		this.ib = ib;
	}

	public String getFlexibleDateSearchFilterType() {
		return flexibleDateSearchFilterType;
	}

	public void setFlexibleDateSearchFilterType(String flexibleDateSearchFilterType) {
		this.flexibleDateSearchFilterType = flexibleDateSearchFilterType;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
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

	public String getProviderResponse() {
		return providerResponse;
	}

	public void setProviderResponse(String providerResponse) {
		this.providerResponse = providerResponse;
	}
}