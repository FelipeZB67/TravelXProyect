package co.edu.unbosque.travelx.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import co.edu.unbosque.travelx.entity.Persona;

public interface PersonaRepository extends CrudRepository<Persona, Long>{
	
	Optional<Persona> findByNombre(String nombre);
	Optional<Persona> findByDocumento(String documento);
	Optional<Persona> findByCorreo(String correo);
	
}
