package ru.otus.bbpax.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.bbpax.entity.Author;

import java.util.Objects;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorView implements EntityView<Author> {
    private String id;
    private String name;
    private String surname;
    private String country;

    public static AuthorView fromEntity(Author author) {
        return Objects.isNull(author)
                ? null
                : new AuthorView(
                    author.getId(),
                    author.getName(),
                    author.getSurname(),
                    author.getCountry()
        );
    }

    @Override
    public Author toEntity() {
        return new Author(id, name, surname, country);
    }
}
