package co.edu.unbosque.travelx.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import co.edu.unbosque.travelx.entity.Reserva;

public interface ReservaRepository extends CrudRepository<Reserva, Long> {

	List<Reserva> findByUsernameOrderByFechaCreacionDesc(String username);
}