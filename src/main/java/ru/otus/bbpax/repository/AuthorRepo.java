package ru.otus.bbpax.repository;

import ru.otus.bbpax.entity.Author;

import java.util.Optional;

/**
 * @author Vlad Rakhlinskii
 * Created on 14.01.2019.
 */
public interface AuthorRepo extends Repo<Author, Long> {
    Optional<Author> findByFullName(String name, String surname);
}
