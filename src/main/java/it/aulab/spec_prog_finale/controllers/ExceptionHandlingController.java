package it.aulab.spec_prog_finale.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.aulab.spec_prog_finale.services.ArticleService;


@Controller
public class ExceptionHandlingController {

    @Autowired
    ArticleService articleService;

    @GetMapping("/error/{number}")
    public String accessDenied(@PathVariable int number,Model model) {
        model.addAttribute("title", "error");
        if(number == 403){
            model.addAttribute("errorMessage", "Non sei atuorizzato!");
        }
        model.addAttribute("articles", articleService.readAll());
        return "home";
    }

}
