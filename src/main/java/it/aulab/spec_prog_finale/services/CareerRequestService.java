package it.aulab.spec_prog_finale.services;

import it.aulab.spec_prog_finale.models.CarreerRequest;
import it.aulab.spec_prog_finale.models.User;

public interface CareerRequestService {
    boolean isRoleAlreadyAssigned(User user, CarreerRequest careerRequest);
    void save(CarreerRequest carrerRequest, User user);
    void carrerAccept(Long requestId);
}

