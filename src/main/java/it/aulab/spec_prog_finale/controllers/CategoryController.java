package it.aulab.spec_prog_finale.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aulab.spec_prog_finale.dtos.ArticleDto;
import it.aulab.spec_prog_finale.models.Article;
import it.aulab.spec_prog_finale.models.Category;
import it.aulab.spec_prog_finale.repositories.CategoryRepository;
import it.aulab.spec_prog_finale.services.CrudService;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    @Qualifier("articleService")
    CrudService<ArticleDto,Article,Long> articleService;

    @Autowired
    CategoryRepository categoryRepository;
    
    @GetMapping("/search/{id}")
    public String articlesSearch(@PathVariable("id") Long id, Model viewModel) {
        Category category = categoryRepository.findById(id).get();
        viewModel.addAttribute("title", "Tutti gli articoli trovati per categoria " + category.getName());
        List<ArticleDto> articles = articleService.searchByCategory(category);
        viewModel.addAttribute("articles", articles);
        return "articles";
    }
}
