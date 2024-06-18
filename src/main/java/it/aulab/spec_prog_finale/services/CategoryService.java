package it.aulab.spec_prog_finale.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import it.aulab.spec_prog_finale.dtos.ArticleDto;
import it.aulab.spec_prog_finale.dtos.CategoryDto;
import it.aulab.spec_prog_finale.models.Article;
import it.aulab.spec_prog_finale.models.Category;
import it.aulab.spec_prog_finale.models.User;
import it.aulab.spec_prog_finale.repositories.CategoryRepository;
import jakarta.transaction.Transactional;

@Service
public class CategoryService implements CrudService<CategoryDto, Category, Long>{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;
    
    @Override
    public List<CategoryDto> readAll() {
        List<CategoryDto> dtos = new ArrayList<CategoryDto>();
        for(Category category: categoryRepository.findAll()){
            dtos.add(modelMapper.map(category, CategoryDto.class));
        }
        return dtos;
    }
    
    @Override
    public CategoryDto read(Long key) {
        return modelMapper.map(categoryRepository.findById(key), CategoryDto.class);
    }
    
    @Override
    public CategoryDto update(Long key, Category model) {
        if (categoryRepository.existsById(key)) {
            model.setId(key);
            return modelMapper.map(categoryRepository.save(model), CategoryDto.class) ;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public CategoryDto create(Category model, Principal principal, MultipartFile file) {
        return modelMapper.map(categoryRepository.save(model), CategoryDto.class);
    }

    @Override
    public List<ArticleDto> searchByCategory(Category category) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchByCategory'");
    }

    @Override
    public List<ArticleDto> searchByAuthor(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchByAuthor'");
    }
    
}
