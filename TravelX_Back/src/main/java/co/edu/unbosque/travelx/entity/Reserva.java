package co.edu.unbosque.travelx.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

/**
 * Entidad que representa una reserva de viaje realizada por un usuario en TravelX,
 * almacenando información del proveedor, origen, destino, fechas, precio,
 * configuración de pasajeros y la respuesta del proveedor externo.
 */
@Entity
@Table(name = "reservatravelx")
public class Reserva {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

	private String username;

	private String provider;
	private String type;
	private String title;

	@Column(length = 1000)
	private String description;

	private String originCity;
	private String originCountry;
	private String destinationCity;
	private String destinationCountry;
	private String departureDate;
	private String returnDate;
	private String currency;
	private Double price;
	private String priceText;
	private Integer adults;
	private Integer children;
	private Integer pets;
	private String travelClass;
	private Boolean hasPool;
	private Boolean hasJacuzzi;
	private Boolean petFriendly;
	private Boolean available;
	private String bookingUrl;
	private Integer providerStatusCode;
	private Boolean providerSuccess;

	@Column(length = 1000)
	private String providerMessage;

	@Lob
	@Column(columnDefinition = "LONGTEXT")
	private String providerResponse;

	private LocalDateTime fechaCreacion;

	/**
	 * Constructor por defecto que inicializa la reserva registrando
	 * automáticamente la fecha y hora de creación.
	 */
	public Reserva() {
		this.fechaCreacion = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOriginCity() {
		return originCity;
	}

	public void setOriginCity(String originCity) {
		this.originCity = originCity;
	}

	public String getOriginCountry() {
		return originCountry;
	}

	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}

	public String getDestinationCity() {
		return destinationCity;
	}

	public void setDestinationCity(String destinationCity) {
		this.destinationCity = destinationCity;
	}

	public String getDestinationCountry() {
		return destinationCountry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getPriceText() {
		return priceText;
	}

	public void setPriceText(String priceText) {
		this.priceText = priceText;
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

	public Integer getPets() {
		return pets;
	}

	public void setPets(Integer pets) {
		this.pets = pets;
	}

	public String getTravelClass() {
		return travelClass;
	}

	public void setTravelClass(String travelClass) {
		this.travelClass = travelClass;
	}

	public Boolean getHasPool() {
		return hasPool;
	}

	public void setHasPool(Boolean hasPool) {
		this.hasPool = hasPool;
	}

	public Boolean getHasJacuzzi() {
		return hasJacuzzi;
	}

	public void setHasJacuzzi(Boolean hasJacuzzi) {
		this.hasJacuzzi = hasJacuzzi;
	}

	public Boolean getPetFriendly() {
		return petFriendly;
	}

	public void setPetFriendly(Boolean petFriendly) {
		this.petFriendly = petFriendly;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public String getBookingUrl() {
		return bookingUrl;
	}

	public void setBookingUrl(String bookingUrl) {
		this.bookingUrl = bookingUrl;
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

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
}