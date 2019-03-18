package ru.otus.bbpax.configuration.changelog.data.builder;

import ru.otus.bbpax.entity.Author;

import java.util.ArrayList;
import java.util.List;

public class AuthorDataBuilder implements DataBuilder<Author> {
    private final List<Author> authors;

    public AuthorDataBuilder() {
        authors = new ArrayList<>();
    }

    @Override
    public DataBuilder<Author> defaultData() {
        authors.add(new Author(
                "1c77bb3f57cfe05a39abc17a",
                "AuthorTest",
                "DoeTest",
                "CountryTest"
        ));
        authors.add(new Author(
                "2c77bb3f57cfe05a39abc17a",
                "Author1",
                "Doe1",
                "USA"
        ));
        authors.add(new Author(
                "3c77bb3f57cfe05a39abc17a",
                "Author2",
                "Doe2",
                "GB"
        ));
        authors.add(new Author(
                "4c77bb3f57cfe05a39abc17a",
                "TestName",
                "TestSurname",
                "TestCountry"
        ));
        return this;
    }

    @Override
    public DataBuilder<Author> with(Author entity) {
        authors.add(entity);
        return this;
    }

    @Override
    public List<Author> build() {
        return authors;
    }
}
