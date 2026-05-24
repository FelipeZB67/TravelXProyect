package co.edu.unbosque.travelx.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import co.edu.unbosque.travelx.entity.Reserva;

/**
 * Repositorio para la gestión de reservas en la base de datos.
 * Extiende {@link CrudRepository} y define métodos de consulta
 * ordenados por fecha de creación descendente.
 */
public interface ReservaRepository extends CrudRepository<Reserva, Long> {

	/**
	 * Busca todas las reservas de un usuario ordenadas de más reciente a más antigua.
	 *
	 * @param username nombre de usuario cuyas reservas se desean consultar
	 * @return lista de reservas del usuario ordenadas por fecha de creación descendente
	 */
	List<Reserva> findByUsernameOrderByFechaCreacionDesc(String username);
	
	/**
	 * Retorna todas las reservas del sistema ordenadas de más reciente a más antigua.
	 *
	 * @return lista de todas las reservas ordenadas por fecha de creación descendente
	 */
	List<Reserva> findAllByOrderByFechaCreacionDesc();
}