package ru.otus.bbpax.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.bbpax.entity.Genre;
import ru.otus.bbpax.repository.GenreRepo;
import ru.otus.bbpax.repository.mapper.GenreMapper;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@Slf4j
@Repository
public class GenreRepoImpl implements GenreRepo {
    private final NamedParameterJdbcOperations jdbc;
    private final GenreMapper mapper;

    public GenreRepoImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
        mapper = new GenreMapper();
    }
    @Override
    public Optional<Genre> findById(Long id) {
        try {
            return Optional.ofNullable(
                    jdbc.queryForObject(
                            "select * from genre where id = :id",
                            Collections.singletonMap("id", id),
                            mapper
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            log.debug("No author was found. {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> getAll() {
        return jdbc.query("select * from genre", mapper);
    }

    @Override
    public void create(Genre entity) {
        int created = jdbc.update(
                "insert into genre (name) values (:name)",
                mapper.mapSource(entity)
        );
        log.info("created {} rows", created);
    }

    @Override
    public void update(Genre entity) {
        int updated = jdbc.update(
                "update genre set name = :name where id = :id",
                mapper.mapSource(entity)
        );
        log.info("updated {} rows", updated);
    }

    @Override
    public void deleteById(Long id) {
        jdbc.queryForObject(
                "delete from genre where id = :id",
                Collections.singletonMap("id", id),
                mapper
        );
    }

    @Override
    public Optional<Genre> findByName(String name) {
        try {
        return Optional.ofNullable(
                jdbc.queryForObject(
                        "select * from genre where name = :name",
                        Collections.singletonMap("name", name),
                        mapper
                ));
        } catch (EmptyResultDataAccessException e) {
            log.warn("Genre '{}' wasn't found.", name);
            return Optional.empty();
        }
    }
}
