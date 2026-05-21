package co.edu.unbosque.travelx.dto;

import java.util.List;

public class AirLabsRouteDTO {

    private String airline_iata;
    private String airline_icao;
    private String flight_number;
    private String flight_iata;
    private String flight_icao;

    private String cs_airline_iata;
    private String cs_flight_iata;
    private String cs_flight_number;

    private String dep_iata;
    private String dep_icao;
    private String dep_time;
    private String dep_time_utc;
    private List<String> dep_terminals;

    private String arr_iata;
    private String arr_icao;
    private String arr_time;
    private String arr_time_utc;
    private List<String> arr_terminals;

    private Integer duration;
    private List<String> days;
    private String aircraft_icao;
    private String updated;

    public AirLabsRouteDTO() {
    }

    public String getAirline_iata() {
        return airline_iata;
    }

    public void setAirline_iata(String airline_iata) {
        this.airline_iata = airline_iata;
    }

    public String getAirline_icao() {
        return airline_icao;
    }

    public void setAirline_icao(String airline_icao) {
        this.airline_icao = airline_icao;
    }

    public String getFlight_number() {
        return flight_number;
    }

    public void setFlight_number(String flight_number) {
        this.flight_number = flight_number;
    }

    public String getFlight_iata() {
        return flight_iata;
    }

    public void setFlight_iata(String flight_iata) {
        this.flight_iata = flight_iata;
    }

    public String getFlight_icao() {
        return flight_icao;
    }

    public void setFlight_icao(String flight_icao) {
        this.flight_icao = flight_icao;
    }

    public String getCs_airline_iata() {
        return cs_airline_iata;
    }

    public void setCs_airline_iata(String cs_airline_iata) {
        this.cs_airline_iata = cs_airline_iata;
    }

    public String getCs_flight_iata() {
        return cs_flight_iata;
    }

    public void setCs_flight_iata(String cs_flight_iata) {
        this.cs_flight_iata = cs_flight_iata;
    }

    public String getCs_flight_number() {
        return cs_flight_number;
    }

    public void setCs_flight_number(String cs_flight_number) {
        this.cs_flight_number = cs_flight_number;
    }

    public String getDep_iata() {
        return dep_iata;
    }

    public void setDep_iata(String dep_iata) {
        this.dep_iata = dep_iata;
    }

    public String getDep_icao() {
        return dep_icao;
    }

    public void setDep_icao(String dep_icao) {
        this.dep_icao = dep_icao;
    }

    public String getDep_time() {
        return dep_time;
    }

    public void setDep_time(String dep_time) {
        this.dep_time = dep_time;
    }

    public String getDep_time_utc() {
        return dep_time_utc;
    }

    public void setDep_time_utc(String dep_time_utc) {
        this.dep_time_utc = dep_time_utc;
    }

    public List<String> getDep_terminals() {
        return dep_terminals;
    }

    public void setDep_terminals(List<String> dep_terminals) {
        this.dep_terminals = dep_terminals;
    }

    public String getArr_iata() {
        return arr_iata;
    }

    public void setArr_iata(String arr_iata) {
        this.arr_iata = arr_iata;
    }

    public String getArr_icao() {
        return arr_icao;
    }

    public void setArr_icao(String arr_icao) {
        this.arr_icao = arr_icao;
    }

    public String getArr_time() {
        return arr_time;
    }

    public void setArr_time(String arr_time) {
        this.arr_time = arr_time;
    }

    public String getArr_time_utc() {
        return arr_time_utc;
    }

    public void setArr_time_utc(String arr_time_utc) {
        this.arr_time_utc = arr_time_utc;
    }

    public List<String> getArr_terminals() {
        return arr_terminals;
    }

    public void setArr_terminals(List<String> arr_terminals) {
        this.arr_terminals = arr_terminals;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    public String getAircraft_icao() {
        return aircraft_icao;
    }

    public void setAircraft_icao(String aircraft_icao) {
        this.aircraft_icao = aircraft_icao;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }
}