package it.aulab.spec_prog_finale.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import it.aulab.spec_prog_finale.dtos.ArticleDto;
import it.aulab.spec_prog_finale.models.Article;
import it.aulab.spec_prog_finale.models.Image;
import it.aulab.spec_prog_finale.repositories.ArticleRepository;
import it.aulab.spec_prog_finale.repositories.ImageRepository;
import it.aulab.spec_prog_finale.utils.StringManipulation;

import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import it.aulab.spec_prog_finale.dtos.ArticleDto;
import it.aulab.spec_prog_finale.models.Article;
import it.aulab.spec_prog_finale.models.User;
import it.aulab.spec_prog_finale.repositories.ArticleRepository;
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
    private ImageRepository imageRepository;

    @Value("${supabase.url}")
    private String supabaseUrl;
    
    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket}")
    private String supabaseBucket;

    @Value("${supabase.image}")
    private String supabaseImage;

    private final RestTemplate restTemplate = new RestTemplate();

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

    @Override
    public ArticleDto create(Article model, MultipartFile file) {
        String urlImage="";
        try {
            urlImage = uploadImage(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArticleDto dto = modelMapper.map(articleRepository.save(model), ArticleDto.class);
        urlImage = urlImage.replace(supabaseBucket, supabaseImage);
        imageRepository.save(Image.builder().path(urlImage).article(model).build());
        return dto;
    }

    public ArticleDto create(Article model, Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = (userRepository.findById(userDetails.getId())).get();
            model.setUser(user);
        }
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
    
    @Async
    public String uploadImage(MultipartFile file) throws Exception {
        String url = "";
        if(!file.isEmpty()){
            try {
                String nameFile = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

                String extension = StringManipulation.getFileExtension(nameFile);

                url = supabaseUrl + supabaseBucket  + nameFile;

                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

                body.add("file", file.getBytes());

                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type","image/"+ extension);
                headers.set("Authorization", "Bearer " + supabaseKey);

                HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);

                restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return url;
    }
}
