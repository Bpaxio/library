package ru.otus.bbpax.repository.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.otus.bbpax.entity.Book;

@Repository
public interface BookReactiveRepo extends ReactiveMongoRepository<Book, String> {
    Flux<Book> findAllByAuthorId(String authorId);

    Flux<Book> findAllByGenreId(String genreId);
}
