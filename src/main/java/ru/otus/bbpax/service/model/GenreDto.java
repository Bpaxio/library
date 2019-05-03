package ru.otus.bbpax.service.model;

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
public class GenreDto implements EntityDto<Genre> {
    private String id;
    private String name;

    public static GenreDto fromEntity(Genre genre) {
        return Objects.isNull(genre)
                ? null
                : new GenreDto(
                        genre.getId(),
                        genre.getName()
                );
    }

    @Override
    public Genre toEntity() {
        return new Genre(id, name);
    }
}
