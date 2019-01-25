package ru.otus.bbpax.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.bbpax.entity.Author;
import ru.otus.bbpax.repository.mapper.AuthorMapper;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@Repository
public class AuthorRepoImpl implements AuthorRepo {
    private final NamedParameterJdbcOperations jdbc;

    public AuthorRepoImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Optional<Author> getById(Long id) {
        return Optional.ofNullable(
                jdbc.queryForObject(
                        "select * from author where id = :id",
                        Collections.singletonMap("id", id),
                        new AuthorMapper()
                )
        );
    }

    @Override
    public List<Author> getAll() {
        throw new NotImplementedException();
    }

    @Override
    public Long update(Author entity) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(Author entity) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteById(Long id) {
        throw new NotImplementedException();
    }
}
