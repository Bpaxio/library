package ru.otus.bbpax.repository;

import ru.otus.bbpax.entity.Genre;

import java.util.Optional;

/**
 * @author Vlad Rakhlinskii
 * Created on 14.01.2019.
 */
public interface GenreRepo extends Repo<Genre, Long> {
    Optional<Genre> findByName(String name);
}
