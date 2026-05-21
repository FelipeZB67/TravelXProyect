package co.edu.unbosque.travelx.dto;

public class AirLabsErrorDTO {

    private String code;
    private String message;

    public AirLabsErrorDTO() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    } 

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}