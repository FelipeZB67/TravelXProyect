package co.edu.unbosque.travelx.dto;

/**
 * Objeto de transferencia de datos que representa un alojamiento tipo Airbnb
 * proveniente de la MockAPI, con información de ubicación, capacidad,
 * precios, amenidades y calificación.
 */
public class MockAirbnbDTO {

	private String id;
	private String nombre;
	private String ciudad;
	private String pais;
	private String tipo_alojamiento;
	private Integer minBedrooms;
	private Integer minBeds;
	private Integer banos;
	private Double priceMin;
	private Double priceMax;
	private Double precio_noche_usd;
	private String amenities;
	private Integer pets;
	private Boolean guesFavorite;
	private Boolean guestFavorite;
	private Double calificacion;

	public MockAirbnbDTO() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getTipo_alojamiento() {
		return tipo_alojamiento;
	}

	public void setTipo_alojamiento(String tipo_alojamiento) {
		this.tipo_alojamiento = tipo_alojamiento;
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

	public Integer getBanos() {
		return banos;
	}

	public void setBanos(Integer banos) {
		this.banos = banos;
	}

	public Double getPriceMin() {
		return priceMin;
	}

	public void setPriceMin(Double priceMin) {
		this.priceMin = priceMin;
	}

	public Double getPriceMax() {
		return priceMax;
	}

	public void setPriceMax(Double priceMax) {
		this.priceMax = priceMax;
	}

	public Double getPrecio_noche_usd() {
		return precio_noche_usd;
	}

	public void setPrecio_noche_usd(Double precio_noche_usd) {
		this.precio_noche_usd = precio_noche_usd;
	}

	public String getAmenities() {
		return amenities;
	}

	public void setAmenities(String amenities) {
		this.amenities = amenities;
	}

	public Integer getPets() {
		return pets;
	}

	public void setPets(Integer pets) {
		this.pets = pets;
	}

	public Boolean getGuesFavorite() {
		return guesFavorite;
	}

	public void setGuesFavorite(Boolean guesFavorite) {
		this.guesFavorite = guesFavorite;
	}

	public Boolean getGuestFavorite() {
		return guestFavorite;
	}

	public void setGuestFavorite(Boolean guestFavorite) {
		this.guestFavorite = guestFavorite;
	}

	public Double getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(Double calificacion) {
		this.calificacion = calificacion;
	}

	 	
}