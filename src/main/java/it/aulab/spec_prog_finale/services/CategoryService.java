package it.aulab.spec_prog_finale.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aulab.spec_prog_finale.dtos.CategoryDto;
import it.aulab.spec_prog_finale.models.Category;
import it.aulab.spec_prog_finale.repositories.CategoryRepository;

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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'read'");
    }
    
    @Override
    public CategoryDto update(Long key, Category model) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
    
    @Override
    public void delete(Long key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public CategoryDto create(Category model, Principal principal) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }
    
}
