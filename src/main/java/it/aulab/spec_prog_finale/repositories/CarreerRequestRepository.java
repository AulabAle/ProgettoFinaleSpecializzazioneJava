package it.aulab.spec_prog_finale.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.aulab.spec_prog_finale.models.CarreerRequest;

public interface CarreerRequestRepository extends CrudRepository<CarreerRequest, Long>{
    List<CarreerRequest> findByIsCheckedFalse();

    @Query(value = "SELECT user_id FROM carreer_request", nativeQuery = true)
    List<Long> findAllUserIds();

    @Query(value = "SELECT * FROM carreer_request WHERE user_id = :id ", nativeQuery = true)
    List<CarreerRequest> findByUserId(@Param("id") Long id);
}
