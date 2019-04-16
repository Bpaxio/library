package ru.otus.bbpax.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.bbpax.entity.Book;

import java.util.List;

/**
 * @author Vlad Rakhlinskii
 * Created on 14.01.2019.
 */
@Repository
public interface BookRepo extends MongoRepository<Book, String> {
    Book getBookByAuthorId(String authorId);

    List<Book> getAllByAuthorId(String authorId);

    List<Book> getAllByGenreId(String genreId);
}
