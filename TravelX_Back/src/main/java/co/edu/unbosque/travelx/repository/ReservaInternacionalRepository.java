package co.edu.unbosque.travelx.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.travelx.entity.ReservaInternacional;
@Repository
public interface ReservaInternacionalRepository extends CrudRepository<ReservaInternacional, Long>{

}