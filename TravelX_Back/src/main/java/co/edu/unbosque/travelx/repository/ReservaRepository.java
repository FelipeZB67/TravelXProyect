package co.edu.unbosque.travelx.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.travelx.entity.Reserva;
@Repository
public interface ReservaRepository extends CrudRepository<Reserva, Long>{

}
