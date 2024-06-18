package it.aulab.spec_prog_finale.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.aulab.spec_prog_finale.models.CarreerRequest;

public interface CarreerRequestRepository extends CrudRepository<CarreerRequest, Long>{
    List<CarreerRequest> findByIsCheckedFalse();
}
