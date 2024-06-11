package it.aulab.spec_prog_finale.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import it.aulab.spec_prog_finale.models.Image;

public interface ImageRepository extends JpaRepository<Image,Long> {
}