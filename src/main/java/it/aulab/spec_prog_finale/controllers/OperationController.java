package it.aulab.spec_prog_finale.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.spec_prog_finale.models.CarreerRequest;
import it.aulab.spec_prog_finale.models.Role;
import it.aulab.spec_prog_finale.models.User;
import it.aulab.spec_prog_finale.repositories.CarreerRequestRepository;
import it.aulab.spec_prog_finale.repositories.RoleRepository;
import it.aulab.spec_prog_finale.repositories.UserRepository;
import it.aulab.spec_prog_finale.services.CareerRequestService;

@Controller
@RequestMapping("/operations")
public class OperationController {

    @Autowired
    private CarreerRequestRepository carreerRequestRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CareerRequestService careerRequestService;

    //Rotta per la creazione di una richiesta di collaborazione
    @GetMapping("/carrer/request")
    public String carrerRequestCreate(Model viewModel) {
        viewModel.addAttribute("title", "Inserisci la tua richiesta");
        viewModel.addAttribute("carrerRequest", new CarreerRequest());

        List<Role> roles = roleRepository.findAll();
        //Elimino la possibilità di scegliere il ruolo user nella select del form
        roles.removeIf(e -> e.getName().equals("ROLE_USER"));  
        viewModel.addAttribute("roles", roles);

        return "carrer/requestForm";
    }

    //Rotta per il salvataggio di una richiesta di ruolo
    @PostMapping("/carrer/request/save")
    public String carrerRequestStore(@ModelAttribute("carrerRequest") CarreerRequest carrerRequest, Principal principal, RedirectAttributes redirectAttributes) {

        User user = userRepository.findByEmail(principal.getName());

        if(careerRequestService.isRoleAlreadyAssigned(user, carrerRequest)){
            redirectAttributes.addFlashAttribute("errorMessage", "Sei già assegnato a questo ruolo");
            return "redirect:/";
        }

        careerRequestService.save(carrerRequest, user);

        redirectAttributes.addFlashAttribute("successMessage", "Richiesta inviata con successo");

        return "redirect:/";
    }

    //Rotta per il dettaglio di una richiesta
    @GetMapping("/carrer/request/detail/{id}")
    public String carrerRequestDetail(@PathVariable("id") Long id, Model viewModel) {
        viewModel.addAttribute("title", "Dettaglio richiesta");
        viewModel.addAttribute("request", carreerRequestRepository.findById(id).get());
        return "carrer/requestDetail";
    }

    //Rotta per l'accettazione di una richiesta
    @PostMapping("/carrer/request/accept/{requestId}")
    public String carrerRequestAccept(@PathVariable Long requestId, RedirectAttributes redirectAttributes) {
        
        careerRequestService.carrerAccept(requestId);
        redirectAttributes.addFlashAttribute("successMessage", "Ruolo abilitato per l'utente");

        return "redirect:/admin/dashboard";
    }
}
