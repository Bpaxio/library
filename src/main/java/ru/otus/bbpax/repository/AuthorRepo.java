package ru.otus.bbpax.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.otus.bbpax.entity.Author;

import java.util.List;
import java.util.Optional;

/**
 * @author Vlad Rakhlinskii
 * Created on 14.01.2019.
 */
@Repository
public interface AuthorRepo extends PagingAndSortingRepository<Author, Long> {

    @Query(value = "select a from Author a where a.name = ?1 and a.surname = ?2")
    Optional<Author> findByFullName(String name, String surname);

    List<Author> findAll();
}
