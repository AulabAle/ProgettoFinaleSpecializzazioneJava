package it.aulab.spec_prog_finale.services;

import java.security.Principal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import it.aulab.spec_prog_finale.models.Category;
import it.aulab.spec_prog_finale.models.User;

public interface CrudService<ReadDto, Model, Key> {
    List<ReadDto> readAll(); 
    ReadDto read(Key key);
    ReadDto create(Model model, Principal principal, MultipartFile file);
    ReadDto update(Key key, Model model);
    void delete(Key key);
    List<ReadDto> searchByCategory(Category category);
    List<ReadDto> searchByAuthor(User user);
    List<ReadDto> search(String keyword);
}

