package co.edu.unbosque.travelx.dto;

import com.google.gson.annotations.SerializedName;

public class NominatimLocationDTO {

	@SerializedName("place_id")
	private long placeId;

	@SerializedName("display_name")
	private String displayName;

	@SerializedName("lat")
	private String lat;

	@SerializedName("lon")
	private String lon;

	@SerializedName("type")
	private String type;

	@SerializedName("class")
	private String category;
	
	public long getPlaceId() {
		return placeId;
	}

	public void setPlaceId(long placeId) {
		this.placeId = placeId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "NominatimLocationDTO{" + "placeId=" + placeId + ", displayName='" + displayName + '\'' + ", lat='" + lat
				+ '\'' + ", lon='" + lon + '\'' + ", type='" + type + '\'' + ", category='" + category + '\'' + '}';
	}
}