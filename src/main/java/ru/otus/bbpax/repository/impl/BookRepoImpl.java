package ru.otus.bbpax.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.repository.BookRepo;
import ru.otus.bbpax.repository.mapper.BookMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
@Slf4j
@Repository
public class BookRepoImpl implements BookRepo {
    private final NamedParameterJdbcOperations jdbc;
    private final BookMapper mapper;

    public BookRepoImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
        mapper = new BookMapper();
    }
    @Override
    public Optional<Book> findById(Long id) {
        try {
            return Optional.ofNullable(
                    jdbc.queryForObject(
                            "select book.id, book.name, publication_date, publishing_office, price, author_id, a.name as author_name, a.surname as author_surname, a.country as author_country, genre_id, g.name as genre_name"
                                    + " from book"
                                    + " join author as a on author_id = a.id"
                                    + " join genre as g on genre_id = g.id"
                                    + " where book.id = :id",
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
    public List<Book> getAll() {
        return jdbc.query(
                "select book.id, book.name, publication_date, publishing_office, price, author_id, a.name as author_name, a.surname as author_surname, a.country as author_country, genre_id, g.name as genre_name"
                        + " from book"
                        + " join author as a on author_id = a.id"
                        + " join genre as g on genre_id = g.id",
                mapper
        );
    }

    @Override
    public void create(Book entity) {
        jdbc.update(
                "insert into book (name, publication_date, publishing_office, price, author_id, genre_id) "
                        + "values (:name, :publication_date, :publishing_office, :price, :author_id, :genre_id)",
                mapper.mapSource(entity)
        );
    }

    @Override
    public void update(Book entity) {
        jdbc.update(
                "update book set name = :name, publication_date = :publication_date, "
                        + "price = :price, publishing_office = :publishing_office, author_id = :author_id, genre_id = :genre_id  "
                        + "where id = :id",
                mapper.mapSource(entity)
        );
    }

    @Override
    public void deleteById(Long id) {
        jdbc.queryForObject(
                "delete from book where id = :id",
                Collections.singletonMap("id", id),
                mapper
        );
    }
}
