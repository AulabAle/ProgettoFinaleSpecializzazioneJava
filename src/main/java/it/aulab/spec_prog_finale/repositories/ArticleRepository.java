package it.aulab.spec_prog_finale.repositories;

import org.springframework.data.repository.ListCrudRepository;

import it.aulab.spec_prog_finale.models.Article;

public interface ArticleRepository extends ListCrudRepository<Article, Long>{
    
}
