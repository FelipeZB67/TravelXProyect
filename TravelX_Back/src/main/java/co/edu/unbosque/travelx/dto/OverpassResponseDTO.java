package co.edu.unbosque.travelx.dto;

import java.util.List;

public class OverpassResponseDTO {

    private Double version;
    private String generator;
    private OverpassOsm3sDTO osm3s;
    private List<OverpassElementDTO> elements;

    public OverpassResponseDTO() {
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public OverpassOsm3sDTO getOsm3s() {
        return osm3s;
    }

    public void setOsm3s(OverpassOsm3sDTO osm3s) {
        this.osm3s = osm3s;
    }

    public List<OverpassElementDTO> getElements() {
        return elements;
    }

    public void setElements(List<OverpassElementDTO> elements) {
        this.elements = elements;
    }
}