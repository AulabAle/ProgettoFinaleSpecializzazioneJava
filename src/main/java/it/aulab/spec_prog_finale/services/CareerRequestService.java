package it.aulab.spec_prog_finale.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.aulab.spec_prog_finale.models.CarreerRequest;
import it.aulab.spec_prog_finale.models.User;
import it.aulab.spec_prog_finale.repositories.CarreerRequestRepository;

import java.util.List;

@Service
public class CareerRequestService {

    @Autowired
    CarreerRequestRepository careerRequestRepository;

    @Transactional
    public boolean isCareerRequestAlreadySubmitted(User user, CarreerRequest careerRequest) {
        List<Long> allUserIds = careerRequestRepository.findAllUserIds();

        if (!allUserIds.contains(user.getId())) {
            return false;
        }

        List<Long> requests = careerRequestRepository.findByUserId(user.getId());

        return requests.stream().anyMatch(roleId -> roleId.equals(careerRequest.getRole().getId()));
    }
}

