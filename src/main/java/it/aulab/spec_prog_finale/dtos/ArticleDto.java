package it.aulab.spec_prog_finale.dtos;

import it.aulab.spec_prog_finale.models.Category;
import it.aulab.spec_prog_finale.models.Image;
import it.aulab.spec_prog_finale.models.User;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Setter
@Getter
@NoArgsConstructor
public class ArticleDto {
    private Long id;
    private String title;
    private String subtitle;
    private String body;
    private String publishDate;
    private User user;
    private Category category;
    private Image image;
}
