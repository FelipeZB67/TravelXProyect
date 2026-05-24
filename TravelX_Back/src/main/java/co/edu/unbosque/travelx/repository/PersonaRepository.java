package co.edu.unbosque.travelx.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import co.edu.unbosque.travelx.entity.Persona;

/**
 * Repositorio para la gestión de personas en la base de datos.
 * Extiende {@link CrudRepository} y define métodos de búsqueda
 * por nombre, documento y correo electrónico.
 */
public interface PersonaRepository extends CrudRepository<Persona, Long>{
	
	/**
	 * Busca una persona por su nombre de usuario.
	 *
	 * @param nombre nombre de usuario a buscar
	 * @return {@link Optional} con la persona encontrada, o vacío si no existe
	 */
	Optional<Persona> findByNombre(String nombre);
	
	/**
	 * Busca una persona por su número de documento de identidad.
	 *
	 * @param documento número de documento a buscar
	 * @return {@link Optional} con la persona encontrada, o vacío si no existe
	 */
	Optional<Persona> findByDocumento(String documento);
	
	/**
	 * Busca una persona por su correo electrónico.
	 *
	 * @param correo correo electrónico a buscar
	 * @return {@link Optional} con la persona encontrada, o vacío si no existe
	 */
	Optional<Persona> findByCorreo(String correo);
	
}
