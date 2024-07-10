package it.aulab.spec_prog_finale.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.spec_prog_finale.dtos.ArticleDto;
import it.aulab.spec_prog_finale.models.Category;
import it.aulab.spec_prog_finale.repositories.CategoryRepository;
import it.aulab.spec_prog_finale.services.ArticleService;
import it.aulab.spec_prog_finale.services.CategoryService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;
    
    //Rotta per la ricerca dell'articolo in base alla categoria
    @GetMapping("/search/{id}")
    public String categorySearch(@PathVariable("id") Long id, Model viewModel) {
        Category category = categoryRepository.findById(id).get();
        viewModel.addAttribute("title", "Tutti gli articoli trovati per categoria " + category.getName());

        List<ArticleDto> articles = articleService.searchByCategory(category);
        viewModel.addAttribute("articles", articles);

        return "article/articles";
    }

    //Rotta per la creazione di una categoria
    @GetMapping("create")
    public String categoryCreate(Model viewModel) {
        viewModel.addAttribute("title", "Crea un categoria");
        viewModel.addAttribute("category", new Category());
        return "category/create";
    }

    //Rotta per la memorizzazione di una categoria
    @PostMapping
    public String categoryStore(@Valid @ModelAttribute("category") Category category, 
                                BindingResult result, 
                                RedirectAttributes redirectAttributes, 
                                Model viewModel) {
        
        //Controllo degli errori con validazioni
        if (result.hasErrors()) {
            viewModel.addAttribute("title", "Crea un categoria");
            viewModel.addAttribute("category", category);
            return "category/create";
        }

        categoryService.create(category, null, null);
        redirectAttributes.addFlashAttribute("successMessage", "Categoria aggiunta con successo!");

        return "redirect:/admin/dashboard"; 
    }

    //Rotta per la modifica di una categoria
    @GetMapping("/edit/{id}")
    public String categoryDetail(@PathVariable("id") Long id, Model viewModel) {
        viewModel.addAttribute("title", "Modifca categoria");
        viewModel.addAttribute("category", categoryService.read(id));
        return "category/update";
    }

    //Rotta per la memorizzazione delle modifiche di una categoria
    @PostMapping("/update/{id}")
    public String categoryUpdate(@PathVariable("id")Long id, 
                                @Valid @ModelAttribute("category") Category category, 
                                BindingResult result, 
                                RedirectAttributes redirectAttributes, 
                                Model viewModel) {
        
        //Controllo degli errori con validazioni
        if (result.hasErrors()) {
            viewModel.addAttribute("title", "Modifca categoria");
            viewModel.addAttribute("category", category);
            return "category/update";
        }

        categoryService.update(id, category, null);
        redirectAttributes.addFlashAttribute("successMessage", "Categoria modificata con successo!");

        return "redirect:/admin/dashboard"; 
    }

    //Rotta per la cancellazione di una categoria
    @GetMapping("delete/{id}")
    public String categoryDelete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
    
        categoryService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Categoria cancellata con successo!");
    
        return "redirect:/admin/dashboard";
    }
}
