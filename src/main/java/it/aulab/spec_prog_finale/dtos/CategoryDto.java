package it.aulab.spec_prog_finale.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private Integer numberOfArticles;
}