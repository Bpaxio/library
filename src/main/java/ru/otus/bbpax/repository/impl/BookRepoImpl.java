package ru.otus.bbpax.repository.impl;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.bbpax.entity.Book;
import ru.otus.bbpax.repository.BookRepo;
import ru.otus.bbpax.repository.mapper.BookMapper;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Vlad Rakhlinskii
 * Created on 10.01.2019.
 */
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
        return Optional.ofNullable(
                jdbc.queryForObject(
                        "select * from book where id = :id",
                        Collections.singletonMap("id", id),
                        mapper
                )
        );
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query("select * from book", mapper);
    }

    @Override
    public void create(Book entity) {
        jdbc.update(
                "insert into book (name, publication_date, price, author_id, genre_id) "
                        + "values (:name, :publication_date, :price, :author_id, :genre_id)",
                mapper.mapSource(entity)
        );
    }

    @Override
    public void update(Book entity) {
        jdbc.update(
                "update book set name = :name, publication_date = :publication_date, "
                        + "price = :price, author_id = :author_id, genre_id = :genre_id  "
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
