package co.edu.unbosque.travelx.dto;

import java.util.List;
import java.util.Map;

public class AirLabsRoutesResponseDTO {

    private Map<String, Object> request;
    private List<AirLabsRouteDTO> response;
    private String terms;

    public AirLabsRoutesResponseDTO() {
    }

    public Map<String, Object> getRequest() {
        return request;
    }

    public void setRequest(Map<String, Object> request) {
        this.request = request;
    }

    public List<AirLabsRouteDTO> getResponse() {
        return response;
    }

    public void setResponse(List<AirLabsRouteDTO> response) {
        this.response = response;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }
}