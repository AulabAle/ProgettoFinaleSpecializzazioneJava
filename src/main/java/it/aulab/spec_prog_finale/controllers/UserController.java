package it.aulab.spec_prog_finale.controllers;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import it.aulab.spec_prog_finale.dtos.ArticleDto;
import it.aulab.spec_prog_finale.dtos.UserDto;
import it.aulab.spec_prog_finale.models.Article;
import it.aulab.spec_prog_finale.models.Category;
import it.aulab.spec_prog_finale.models.User;
import it.aulab.spec_prog_finale.repositories.UserRepository;
import it.aulab.spec_prog_finale.services.CrudService;
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

    @Autowired
    @Qualifier("articleService")
    CrudService<ArticleDto,Article,Long> articleService;

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/")
    public String home(Model model, Principal principal) {
        if(principal != null){
            CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("userdetail", customUserDetails);
        }else{
            model.addAttribute("userdetail", null);
        }
        List<ArticleDto> articles = articleService.readAll();
        Collections.sort(articles, Comparator.comparing(ArticleDto::getPublishDate).reversed());
        model.addAttribute("articles", articles);
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

    @GetMapping("/search/{id}")
    public String articlesSearch(@PathVariable("id") Long id, Model viewModel) {
        User user = userRepository.findById(id).get();
        viewModel.addAttribute("title", "Tutti gli articoli trovati per utente " + user.getUsername());
        List<ArticleDto> articles = articleService.searchByAuthor(user);
        viewModel.addAttribute("articles", articles);
        return "articles";
    }
}
