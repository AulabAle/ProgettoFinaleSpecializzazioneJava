package it.aulab.spec_prog_finale.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import it.aulab.spec_prog_finale.models.Image;
import jakarta.transaction.Transactional;

public interface ImageRepository extends JpaRepository<Image,Long> {
    @Transactional
    void deleteByPath(String imageUrl);
}