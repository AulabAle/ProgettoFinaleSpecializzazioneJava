package it.aulab.spec_prog_finale.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface CrudService<ReadDto, Model, Key> {
    List<ReadDto> readAll(); 
    ReadDto read(Key key);
    ReadDto create(Model model);
    ReadDto create(Model model, MultipartFile file);
    ReadDto update(Key key, Model model);
    void delete(Key key);
}

