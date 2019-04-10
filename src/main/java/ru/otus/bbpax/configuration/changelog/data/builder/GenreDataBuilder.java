package ru.otus.bbpax.configuration.changelog.data.builder;

import ru.otus.bbpax.entity.Genre;

import java.util.ArrayList;
import java.util.List;

public class GenreDataBuilder implements DataBuilder<Genre> {
    private final List<Genre> genres;

    public GenreDataBuilder() {
        genres = new ArrayList<>();
    }

    @Override
    public DataBuilder<Genre> defaultData() {
        genres.add(new Genre(
                "1c77bb3f57cfe05a39abc17a",
                "Novel"
        ));
        genres.add(new Genre(
                "2c77bb3f57cfe05a39abc17a",
                "Drama"
        ));
        genres.add(new Genre(
                "3c77bb3f57cfe05a39abc17a",
                "Science fiction"
        ));
        genres.add(new Genre(
                "4c77bb3f57cfe05a39abc17a",
                "TestGenre"
        ));
        return this;
    }

    @Override
    public DataBuilder<Genre> with(Genre entity) {
        genres.add(entity);
        return this;
    }

    @Override
    public List<Genre> build() {
        return genres;
    }
}
