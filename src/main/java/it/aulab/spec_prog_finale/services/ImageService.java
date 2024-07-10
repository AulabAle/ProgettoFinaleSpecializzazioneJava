package it.aulab.spec_prog_finale.services;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.multipart.MultipartFile;

import it.aulab.spec_prog_finale.models.Article;


public interface ImageService {
    void saveImageOnDB(String url, Article article);
    CompletableFuture<String> saveImageOnCloud(MultipartFile file) throws Exception;
    void deleteImage(String imagePath) throws IOException;
}
