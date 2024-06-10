package it.aulab.spec_prog_finale.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import it.aulab.spec_prog_finale.dtos.UserDto;
import it.aulab.spec_prog_finale.models.User;
import it.aulab.spec_prog_finale.services.CustomUserDetails;
import it.aulab.spec_prog_finale.services.CustomUserDetailsService;
import it.aulab.spec_prog_finale.services.UserService;
import jakarta.validation.Valid;

@Controller
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @GetMapping("/")
    public String home(Model model, Principal principal) {
        if(principal != null){
            CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("userdetail", customUserDetails);
        }else{
            model.addAttribute("userdetail", null);
        }
        return "home";
    }
    
    @GetMapping("/register")
    public String register(Model model) {
        // create model object to store form data
        model.addAttribute("user", new UserDto());
        return "register";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model) {

        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }
}
