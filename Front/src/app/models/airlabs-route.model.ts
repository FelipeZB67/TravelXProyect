export interface AirLabsRouteModel {
  airline_iata:    string;
  airline_icao:    string;
  flight_number:   string;
  flight_iata:     string;
  flight_icao:     string;
  dep_iata:        string;
  dep_icao:        string;
  dep_time:        string;
  dep_time_utc:    string;
  dep_terminals:   string[];
  arr_iata:        string;
  arr_icao:        string;
  arr_time:        string;
  arr_time_utc:    string;
  arr_terminals:   string[];
  duration:        number;
  days:            string[];
  aircraft_icao:   string;
  updated:         string;
}
