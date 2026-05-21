package co.edu.unbosque.travelx.dto;

import java.util.List;
import java.util.Map;

public class OverpassElementDTO {

    private String type;
    private Long id;

    private Double lat;
    private Double lon;

    private OverpassCenterDTO center;

    private List<Long> nodes;

    private Map<String, String> tags;

    public OverpassElementDTO() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public OverpassCenterDTO getCenter() {
        return center;
    }

    public void setCenter(OverpassCenterDTO center) {
        this.center = center;
    }

    public List<Long> getNodes() {
        return nodes;
    }

    public void setNodes(List<Long> nodes) {
        this.nodes = nodes;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }
}