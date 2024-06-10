package it.aulab.spec_prog_finale.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.spec_prog_finale.models.Article;
import it.aulab.spec_prog_finale.services.ArticleService;
import it.aulab.spec_prog_finale.services.CategoryService;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleService articleService;

    @GetMapping
    public String articlesIndex(Model viewModel) {
        viewModel.addAttribute("title", "Tutti gli articoli");
        viewModel.addAttribute("articles", articleService.readAll());
        return "articles";
    }

    //Rotta per la creazione di un articolo
    @GetMapping("create")
    public String articleCreate(Model viewModel) {
        viewModel.addAttribute("title", "Crea un articolo");
        viewModel.addAttribute("article", new Article());
        viewModel.addAttribute("categories", categoryService.readAll());
        return "createArticle";
    }
    
    @PostMapping
    public String articleStore(@ModelAttribute("article") Article article, RedirectAttributes redirectAttributes) {
        articleService.create(article);
        redirectAttributes.addFlashAttribute("successMessage", "Articolo aggiunto con successo!");
        return "redirect:/articles"; 
    }
}
