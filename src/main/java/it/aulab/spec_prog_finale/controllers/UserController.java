package it.aulab.spec_prog_finale.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import it.aulab.spec_prog_finale.dtos.ArticleDto;
import it.aulab.spec_prog_finale.dtos.UserDto;
import it.aulab.spec_prog_finale.models.Article;
import it.aulab.spec_prog_finale.models.User;
import it.aulab.spec_prog_finale.repositories.ArticleRepository;
import it.aulab.spec_prog_finale.repositories.CarreerRequestRepository;
import it.aulab.spec_prog_finale.repositories.UserRepository;
import it.aulab.spec_prog_finale.services.CategoryService;
import it.aulab.spec_prog_finale.services.CrudService;
import it.aulab.spec_prog_finale.services.CustomUserDetails;
import it.aulab.spec_prog_finale.services.CustomUserDetailsService;
import it.aulab.spec_prog_finale.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    @Qualifier("articleService")
    private CrudService<ArticleDto,Article,Long> articleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarreerRequestRepository carreerRequestRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryService categoryService;
    
    //Rotta di home
    @GetMapping("/")
    public String home(Model model, Principal principal) {
        //Recupero il contesto di autenticazione per la verifica di avvenuto login o register
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(principal != null || auth.getPrincipal() != "anonymousUser"){
            CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("userdetail", customUserDetails);
        }else{
            model.addAttribute("userdetail", null);
        }

        //Recupero tutti gli articoli accettati
        List<ArticleDto> articles = new ArrayList<ArticleDto>();
        for(Article article: articleRepository.findByIsAcceptedTrue()){
            articles.add(modelMapper.map(article, ArticleDto.class));
        }

        //ordino e invio al template gli articoli ordinati in modo decrescente
        Collections.sort(articles, Comparator.comparing(ArticleDto::getPublishDate).reversed());
        model.addAttribute("articles", articles);

        return "home";
    }
    
    //Rotta per la registrazione
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserDto());
        return "auth/register";
    }
    
    //Rotta per la login
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
    
    //Rotta per il salvataggio della registrazione
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model, 
                               RedirectAttributes redirectAttributes,
                               HttpServletRequest request, HttpServletResponse response){

        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "auth/register";
        }

        userService.saveUser(userDto, redirectAttributes, request, response);

        redirectAttributes.addFlashAttribute("successMessage", "Registrazione avvenuta!");

        return "redirect:/";
    }

    //Rotta per la ricerca degli articoli in base all'utente
    @GetMapping("/search/{id}")
    public String userArticlesSearch(@PathVariable("id") Long id, Model viewModel) {
        User user = userRepository.findById(id).get();
        viewModel.addAttribute("title", "Tutti gli articoli trovati per utente " + user.getUsername());

        List<ArticleDto> articles = articleService.searchByAuthor(user);
        viewModel.addAttribute("articles", articles);

        return "article/articles";
    }

    //Rotta per la dashboard dell'admin
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model viewModel) {
        viewModel.addAttribute("title", "Richieste ricevute");
        viewModel.addAttribute("requests", carreerRequestRepository.findByIsCheckedFalse());
        viewModel.addAttribute("categories", categoryService.readAll());
        return "admin/dashboard";
    }

    //Rotta per la dashboard del revisor
    @GetMapping("/revisor/dashboard")
    public String ravisorDashboard(Model viewModel) {
        viewModel.addAttribute("title", "Articoli da revisionare");
        viewModel.addAttribute("articles", articleRepository.findByIsAcceptedFalse());
        return "revisor/dashboard";
    }

    //Rotta per la dashboard del writer
    @GetMapping("/writer/dashboard")
    public String writerDashboard(Model viewModel , Principal principal) {
        viewModel.addAttribute("title", "I tuoi articoli");
        List<ArticleDto> userArticles = articleService.readAll()
                                                      .stream()
                                                      .filter(article -> article.getUser().getEmail().equals(principal.getName()))
                                                      .toList();

        viewModel.addAttribute("articles", userArticles);
        
        return "writer/dashboard";
    }

}
