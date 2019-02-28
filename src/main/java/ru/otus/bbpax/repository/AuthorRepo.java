package ru.otus.bbpax.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.bbpax.entity.Author;

import java.util.Optional;

/**
 * @author Vlad Rakhlinskii
 * Created on 14.01.2019.
 */
@Repository
public interface AuthorRepo extends CommonRepo<Author, Long> {

    @Query(value = "select a from Author a where a.name = :name and a.surname = :surname")
    Optional<Author> findByFullName(@Param("name") String name,
                                    @Param("surname") String surname);
}
