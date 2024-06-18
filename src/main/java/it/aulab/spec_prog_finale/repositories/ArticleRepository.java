package it.aulab.spec_prog_finale.repositories;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;
import it.aulab.spec_prog_finale.models.Article;
import it.aulab.spec_prog_finale.models.Category;
import it.aulab.spec_prog_finale.models.User;

public interface ArticleRepository extends ListCrudRepository<Article, Long>{
    List<Article> findByCategory(Category category);
    List<Article> findByUser(User user);
    List<Article> findByIsAcceptedTrue();
    List<Article> findByIsAcceptedFalse();
}
