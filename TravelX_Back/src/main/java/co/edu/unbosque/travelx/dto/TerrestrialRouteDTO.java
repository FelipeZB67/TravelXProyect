package co.edu.unbosque.travelx.dto;

public class TerrestrialRouteDTO {

	private String origen;
	private String destino;
	private String pais_origen;
	private String pais_destino;
	private String continente;
	private String operador;
	private Double precio_usd;
	private Double duracion_horas;
	private Integer transbordos;
	private String tipo_bus;
	private String frecuencia;
	private Double distancia_km;
	private String id;

	public TerrestrialRouteDTO() {
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getPais_origen() {
		return pais_origen;
	}

	public void setPais_origen(String pais_origen) {
		this.pais_origen = pais_origen;
	}

	public String getPais_destino() {
		return pais_destino;
	}

	public void setPais_destino(String pais_destino) {
		this.pais_destino = pais_destino;
	}

	public String getContinente() {
		return continente;
	}

	public void setContinente(String continente) {
		this.continente = continente;
	}

	public String getOperador() {
		return operador;
	}

	public void setOperador(String operador) {
		this.operador = operador;
	}

	public Double getPrecio_usd() {
		return precio_usd;
	}

	public void setPrecio_usd(Double precio_usd) {
		this.precio_usd = precio_usd;
	}

	public Double getDuracion_horas() {
		return duracion_horas;
	}

	public void setDuracion_horas(Double duracion_horas) {
		this.duracion_horas = duracion_horas;
	}

	public Integer getTransbordos() {
		return transbordos;
	}

	public void setTransbordos(Integer transbordos) {
		this.transbordos = transbordos;
	}

	public String getTipo_bus() {
		return tipo_bus;
	}

	public void setTipo_bus(String tipo_bus) {
		this.tipo_bus = tipo_bus;
	}

	public String getFrecuencia() {
		return frecuencia;
	}

	public void setFrecuencia(String frecuencia) {
		this.frecuencia = frecuencia;
	}

	public Double getDistancia_km() {
		return distancia_km;
	}

	public void setDistancia_km(Double distancia_km) {
		this.distancia_km = distancia_km;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
}