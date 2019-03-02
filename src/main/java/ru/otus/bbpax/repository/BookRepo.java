package ru.otus.bbpax.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.bbpax.entity.Book;

/**
 * @author Vlad Rakhlinskii
 * Created on 14.01.2019.
 */
@Repository
public interface BookRepo extends MongoRepository<Book, String> {
}
