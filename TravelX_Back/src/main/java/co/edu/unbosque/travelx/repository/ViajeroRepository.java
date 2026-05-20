package co.edu.unbosque.travelx.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import co.edu.unbosque.travelx.entity.Viajero;

public interface ViajeroRepository extends CrudRepository<Viajero, Long>{
	
	Optional<Viajero> findByPasaporte(String pasaporte);
}
