package co.edu.unbosque.travelx.dto;

public class OverpassCenterDTO {

    private Double lat;
    private Double lon;

    public OverpassCenterDTO() {
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}