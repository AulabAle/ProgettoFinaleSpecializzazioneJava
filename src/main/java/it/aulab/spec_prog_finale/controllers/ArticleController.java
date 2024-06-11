package it.aulab.spec_prog_finale.controllers;

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
import it.aulab.spec_prog_finale.services.ArticleService;
import it.aulab.spec_prog_finale.services.CategoryService;
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
    public String articleStore(@ModelAttribute("article") Article article, RedirectAttributes redirectAttributes, MultipartFile file) {
        articleService.create(article , file);
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
