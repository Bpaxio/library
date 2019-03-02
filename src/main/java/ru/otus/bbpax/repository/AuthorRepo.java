package ru.otus.bbpax.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.bbpax.entity.Author;

import java.util.Optional;

/**
 * @author Vlad Rakhlinskii
 * Created on 14.01.2019.
 */
@Repository
public interface AuthorRepo extends MongoRepository<Author, String> {
    Optional<Author> findByNameAndSurname(@Param("name") String name,
                                          @Param("surname") String surname);
}
