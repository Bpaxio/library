package ru.otus.bbpax.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.bbpax.entity.Genre;

import java.util.Objects;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreView implements EntityView<Genre> {
    private Long id;
    private String name;

    public static GenreView fromEntity(Genre genre) {
        return Objects.isNull(genre)
                ? null
                : new GenreView(
                        genre.getId(),
                        genre.getName()
                );
    }

    @Override
    public Genre toEntity() {
        return new Genre(id, name);
    }
}
