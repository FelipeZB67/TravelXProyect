package co.edu.unbosque.travelx.dto;

/**
 * Objeto de transferencia de datos que encapsula los parámetros de búsqueda
 * unificada de viajes, permitiendo configurar origen, destino, fechas,
 * pasajeros, tipos de servicio a incluir y filtros de hospedaje.
 */
public class TravelSearchRequestDTO {

	private String ciudadOrigen;
	private String paisOrigen;

	private String ciudadDestino;
	private String paisDestino;

	private String fechaSalida;
	private String fechaRegreso;
	private Boolean incluirTransporteTerrestre;
	
	private Integer adultos;
	private Integer ninos;
	private Integer mascotas;

	private String moneda;
	private String claseViaje;

	private Boolean incluirVuelos;
	private Boolean incluirAirbnb;
	private Boolean incluirHoteles;

	private Integer precioMinimo;
	private Integer precioMaximo;

	private Boolean piscina;
	private Boolean jacuzzi;
	private Boolean petFriendly;

	public TravelSearchRequestDTO() {
	}

	public String getCiudadOrigen() {
		return ciudadOrigen;
	}

	public void setCiudadOrigen(String ciudadOrigen) {
		this.ciudadOrigen = ciudadOrigen;
	}

	public String getPaisOrigen() {
		return paisOrigen;
	}

	public void setPaisOrigen(String paisOrigen) {
		this.paisOrigen = paisOrigen;
	}

	public String getCiudadDestino() {
		return ciudadDestino;
	}

	public void setCiudadDestino(String ciudadDestino) {
		this.ciudadDestino = ciudadDestino;
	}

	public String getPaisDestino() {
		return paisDestino;
	}

	public void setPaisDestino(String paisDestino) {
		this.paisDestino = paisDestino;
	}

	public String getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(String fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	public String getFechaRegreso() {
		return fechaRegreso;
	}

	public void setFechaRegreso(String fechaRegreso) {
		this.fechaRegreso = fechaRegreso;
	}

	public Integer getAdultos() {
		return adultos;
	}

	public void setAdultos(Integer adultos) {
		this.adultos = adultos;
	}

	public Integer getNinos() {
		return ninos;
	}

	public void setNinos(Integer ninos) {
		this.ninos = ninos;
	}

	public Integer getMascotas() {
		return mascotas;
	}

	public void setMascotas(Integer mascotas) {
		this.mascotas = mascotas;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getClaseViaje() {
		return claseViaje;
	}

	public void setClaseViaje(String claseViaje) {
		this.claseViaje = claseViaje;
	}

	public Boolean getIncluirVuelos() {
		return incluirVuelos;
	}

	public void setIncluirVuelos(Boolean incluirVuelos) {
		this.incluirVuelos = incluirVuelos;
	}

	public Boolean getIncluirAirbnb() {
		return incluirAirbnb;
	}

	public void setIncluirAirbnb(Boolean incluirAirbnb) {
		this.incluirAirbnb = incluirAirbnb;
	}

	public Boolean getIncluirHoteles() {
		return incluirHoteles;
	}

	public void setIncluirHoteles(Boolean incluirHoteles) {
		this.incluirHoteles = incluirHoteles;
	}

	public Integer getPrecioMinimo() {
		return precioMinimo;
	}

	public void setPrecioMinimo(Integer precioMinimo) {
		this.precioMinimo = precioMinimo;
	}

	public Integer getPrecioMaximo() {
		return precioMaximo;
	}

	public void setPrecioMaximo(Integer precioMaximo) {
		this.precioMaximo = precioMaximo;
	}

	public Boolean getPiscina() {
		return piscina;
	}

	public void setPiscina(Boolean piscina) {
		this.piscina = piscina;
	}

	public Boolean getJacuzzi() {
		return jacuzzi;
	}

	public void setJacuzzi(Boolean jacuzzi) {
		this.jacuzzi = jacuzzi;
	}

	public Boolean getPetFriendly() {
		return petFriendly;
	}

	public void setPetFriendly(Boolean petFriendly) {
		this.petFriendly = petFriendly;
	}

	public Boolean getIncluirTransporteTerrestre() {
		return incluirTransporteTerrestre;
	}

	public void setIncluirTransporteTerrestre(Boolean incluirTransporteTerrestre) {
		this.incluirTransporteTerrestre = incluirTransporteTerrestre;
	}

	
}