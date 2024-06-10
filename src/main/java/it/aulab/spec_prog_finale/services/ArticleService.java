package it.aulab.spec_prog_finale.services;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.aulab.spec_prog_finale.dtos.ArticleDto;
import it.aulab.spec_prog_finale.models.Article;
import it.aulab.spec_prog_finale.repositories.ArticleRepository;

@Service
public class ArticleService implements CrudService<ArticleDto, Article, Long>{

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ArticleDto> readAll() {
        List<ArticleDto> dtos = new ArrayList<ArticleDto>();
        for(Article article: articleRepository.findAll()){
            dtos.add(modelMapper.map(article, ArticleDto.class));
        }
        return dtos;
    }

    @Override
    public ArticleDto read(Long key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'read'");
    }

    @Override
    public ArticleDto create(Article model) {
        return modelMapper.map(articleRepository.save(model), ArticleDto.class);
    }

    @Override
    public ArticleDto update(Long key, Article model) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Long key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
}
