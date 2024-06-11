package it.aulab.spec_prog_finale.controllers;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.spec_prog_finale.dtos.ArticleDto;
import it.aulab.spec_prog_finale.dtos.CategoryDto;
import it.aulab.spec_prog_finale.models.Article;
import it.aulab.spec_prog_finale.models.Category;
import it.aulab.spec_prog_finale.services.CrudService;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    @Qualifier("categoryService")
    CrudService<CategoryDto,Category,Long> categoryService;

    @Autowired
    @Qualifier("articleService")
    CrudService<ArticleDto,Article,Long> articleService;

    @GetMapping
    public String articlesIndex(Model viewModel) {
        viewModel.addAttribute("title", "Tutti gli articoli");
        //viewModel.addAttribute("articles", articleService.readAll());
        List<ArticleDto> articles = articleService.readAll();
        Collections.sort(articles, Comparator.comparing(ArticleDto::getPublishDate).reversed());
        viewModel.addAttribute("articles", articles);
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
    public String articleStore(@ModelAttribute("article") Article article, RedirectAttributes redirectAttributes, Principal principal, MultipartFile file) {
        articleService.create(article, principal, file);
        redirectAttributes.addFlashAttribute("successMessage", "Articolo aggiunto con successo!");
        return "redirect:/articles"; 
    }

    @GetMapping("detail/{id}")
    public String detailAuthorView(@PathVariable("id") Long id, Model viewModel) {
        viewModel.addAttribute("title", "Article detail");
        viewModel.addAttribute("article", articleService.read(id));
        return "articleDetail";
    }
}