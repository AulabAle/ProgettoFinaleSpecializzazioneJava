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
import it.aulab.spec_prog_finale.services.CustomUserDetailsService;

@Controller
@RequestMapping("/operations")
public class OperationController {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    CarreerRequestRepository carreerRequestRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    //Rotta per la creazione di un articolo
    @GetMapping("/carrer/request")
    public String carrerRequestCreate(Model viewModel) {
        viewModel.addAttribute("title", "Inserisci la tua richiesta");
        viewModel.addAttribute("carrerRequest", new CarreerRequest());
        List<Role> roles = roleRepository.findAll();
        roles.removeIf(e -> e.getName().equals("ROLE_USER"));  
        viewModel.addAttribute("roles", roles);
        return "careerRequestForm";
    }

    @PostMapping("/carrer/request/save")
    public String carrerRequestStore(@ModelAttribute("carrerRequest") CarreerRequest carrerRequest, Principal principal, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByEmail(principal.getName());
        carrerRequest.setUser(user);
        carrerRequest.setIsChecked(false);
        carreerRequestRepository.save(carrerRequest);
        redirectAttributes.addFlashAttribute("successMessage", "Richiesta inviata con successo");
        return "redirect:/";
    }

    @GetMapping("/carrer/request/detail/{id}")
    public String carrerRequestDetail(@PathVariable("id") Long id, Model viewModel) {
        viewModel.addAttribute("title", "Dettaglio richiesta");
        viewModel.addAttribute("request", carreerRequestRepository.findById(id).get());
        return "careerRequestDetail";
    }

    @PostMapping("/carrer/request/accept/{requestId}")
    public String carrerRequestAccept(@PathVariable Long requestId, RedirectAttributes redirectAttributes) {
        CarreerRequest request = carreerRequestRepository.findById(requestId).get();
        User user = request.getUser();
        Role role = request.getRole();
        List<Role> rolesUser = user.getRoles();
        Role newRole = roleRepository.findByName(role.getName());
        rolesUser.add(newRole);
        user.setRoles(rolesUser);
        userRepository.save(user);
        request.setIsChecked(true);
        carreerRequestRepository.save(request);
        redirectAttributes.addFlashAttribute("successMessage", "Ruolo abilitato per l'utente");
        return "redirect:/admin/dashboard";
    }
}
