package co.edu.unbosque.travelx.dto;

public class AirbnbSearchRequestDTO {

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

	public AirbnbSearchRequestDTO() {
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
}