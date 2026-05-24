package co.edu.unbosque.travelx.dto;

/**
 * Objeto de transferencia de datos que encapsula los parámetros necesarios
 * para consultar el detalle de un hotel, incluyendo fechas de estancia,
 * número de huéspedes, moneda e idioma.
 */
public class HotelDetailsRequestDTO {

	private Long id;
	private String checkIn;
	private String checkOut;
	private Integer adults1;
	private String children1;
	private String currency;
	private String locale;

	public HotelDetailsRequestDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCheckIn() {
		return checkIn;
	}

	public void setCheckIn(String checkIn) {
		this.checkIn = checkIn;
	}

	public String getCheckOut() {
		return checkOut;
	}

	public void setCheckOut(String checkOut) {
		this.checkOut = checkOut;
	}

	public Integer getAdults1() {
		return adults1;
	}

	public void setAdults1(Integer adults1) {
		this.adults1 = adults1;
	}

	public String getChildren1() {
		return children1;
	}

	public void setChildren1(String children1) {
		this.children1 = children1;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
}