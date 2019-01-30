package ru.otus.bbpax.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.bbpax.entity.Author;
import ru.otus.bbpax.repository.AuthorRepo;
import ru.otus.bbpax.repository.mapper.AuthorMapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@Slf4j
@Repository
public class AuthorRepoImpl implements AuthorRepo {
    private final NamedParameterJdbcOperations jdbc;
    private final AuthorMapper mapper;

    public AuthorRepoImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
        mapper = new AuthorMapper();
    }

    @Override
    public Optional<Author> findById(Long id) {
        return Optional.ofNullable(
                jdbc.queryForObject(
                        "select * from author where id = :id",
                        Collections.singletonMap("id", id),
                        mapper
                )
        );
    }

    @Override
    public List<Author> getAll() {
        return jdbc.query("select * from author", mapper);
    }

    @Override
    public void create(Author entity) {
        int created = jdbc.update(
                "insert into author (name, surname, country) values (:name, :surname, :country)",
                mapper.mapSource(entity)
        );
        log.info("created {} rows", created);
    }

    @Override
    public void update(Author entity) {
        int updated = jdbc.update(
                "update author set name = :name, surname = :surname, country = :country  where id = :id",
                mapper.mapSource(entity)
        );
        log.info("updated {} rows", updated);
    }

    @Override
    public void deleteById(Long id) {
        jdbc.queryForObject(
                "delete from author where id = :id",
                Collections.singletonMap("id", id),
                mapper
        );
    }

    @Override
    public Optional<Author> findByFullName(String name, String surname) {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("surname", surname);
        try {
            return Optional.ofNullable(
                    jdbc.queryForObject(
                            "select * from author where name = :name and surname = :surname",
                            map,
                            mapper
                    ));
        } catch (EmptyResultDataAccessException e) {
            log.warn("Author '{} {}' wasn't found.", name, surname);
            return Optional.empty();
        }
    }
}
