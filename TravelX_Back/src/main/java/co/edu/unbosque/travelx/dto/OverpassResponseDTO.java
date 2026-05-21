package co.edu.unbosque.travelx.dto;

import java.util.List;

public class OverpassResponseDTO {

    private Double version;
    private String generator;
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

    public List<OverpassElementDTO> getElements() {
        return elements;
    }

    public void setElements(List<OverpassElementDTO> elements) {
        this.elements = elements;
    }
}