package it.aulab.spec_prog_finale.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import it.aulab.spec_prog_finale.dtos.ArticleDto;
import it.aulab.spec_prog_finale.models.Article;
import it.aulab.spec_prog_finale.models.Category;
import it.aulab.spec_prog_finale.models.User;
import it.aulab.spec_prog_finale.repositories.ArticleRepository;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import it.aulab.spec_prog_finale.repositories.UserRepository;

@Service
public class ArticleService implements CrudService<ArticleDto, Article, Long>{

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ImageService imageService;

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
        Optional<Article> optArticle = articleRepository.findById(key);
        if (optArticle.isPresent()) {
            return modelMapper.map(optArticle.get(), ArticleDto.class);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author id=" + key + " not found");
        }
    }

    public ArticleDto create(Article model, Principal principal, MultipartFile file) {
        String url = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = (userRepository.findById(userDetails.getId())).get();
            model.setUser(user);
        }

        if(!file.isEmpty()){
            try {
                CompletableFuture<String> futureUrl = imageService.saveImageOnCloud(file);
                url = futureUrl.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        model.setIsAccepted(false);

        ArticleDto dto = modelMapper.map(articleRepository.save(model), ArticleDto.class);
        if(!file.isEmpty()){
            imageService.saveImageOnDB(url, model);
        }
        return dto;
    }

    @Override
    public ArticleDto update(Long key, Article model, MultipartFile file) {
        String url="";
        if (articleRepository.existsById(key)) {
            model.setId(key);
            Article article = articleRepository.findById(key).get();

            model.setUser(article.getUser());
            if(!file.isEmpty()){
                try {
                    imageService.deleteImage(article.getImage().getPath());
                    try {
                        CompletableFuture<String> futureUrl = imageService.saveImageOnCloud(file);
                        url = futureUrl.get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    imageService.saveImageOnDB(url, model);
                    model.setIsAccepted(false);
                    return modelMapper.map(articleRepository.save(model), ArticleDto.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                
                if(!model.equals(article)){
                    model.setIsAccepted(false);
                }else{
                    model.setIsAccepted(article.getIsAccepted());
                }

                return modelMapper.map(articleRepository.save(model), ArticleDto.class) ;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @Override
    public void delete(Long key) {
        if (articleRepository.existsById(key)) {

            Article article = articleRepository.findById(key).get();

            try {
                imageService.deleteImage(article.getImage().getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }

            article.getImage().setArticle(null);

            articleRepository.deleteById(key);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
    
    public List<ArticleDto> searchByCategory(Category category){
        List<ArticleDto> dtos = new ArrayList<ArticleDto>();
        for(Article article: articleRepository.findByCategory(category)){
            dtos.add(modelMapper.map(article, ArticleDto.class));
        }
        return dtos;
    }

    public List<ArticleDto> searchByAuthor(User user){
        List<ArticleDto> dtos = new ArrayList<ArticleDto>();
        for(Article article: articleRepository.findByUser(user)){
            dtos.add(modelMapper.map(article, ArticleDto.class));
        }
        return dtos;
    }

    public List<ArticleDto> search(String keyword){
        List<ArticleDto> dtos = new ArrayList<ArticleDto>();
        for(Article article: articleRepository.search(keyword)){
            dtos.add(modelMapper.map(article, ArticleDto.class));
        }
        return dtos;
    }

}
