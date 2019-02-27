package ru.otus.bbpax.repository;

import org.springframework.stereotype.Repository;
import ru.otus.bbpax.entity.Genre;

import java.util.Optional;

/**
 * @author Vlad Rakhlinskii
 * Created on 14.01.2019.
 */
@Repository
public interface GenreRepo extends CommonRepo<Genre, Long> {
    Optional<Genre> findByName(String name);
}
